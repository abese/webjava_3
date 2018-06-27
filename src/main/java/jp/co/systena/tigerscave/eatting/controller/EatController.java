package jp.co.systena.tigerscave.eatting.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.eatting.model.Cook;
import jp.co.systena.tigerscave.eatting.model.Customer;
import jp.co.systena.tigerscave.eatting.model.EattingForm;
import jp.co.systena.tigerscave.eatting.model.Food;
import jp.co.systena.tigerscave.eatting.service.DbAccessService;

@Controller
public class EatController {

  // 表示用クラス
  EattingForm form;

  // DBservice
  @Autowired
  DbAccessService dbService = new DbAccessService();

  @RequestMapping("/Eatting")
  public Model show(Model model) {

    // Viewに渡すデータを設定
    model.addAttribute("eattingForm", getEattingForm());

    return model;
  }

  @RequestMapping(value = "/Eatting", params="order" , method = RequestMethod.POST)
  public ModelAndView eating(EattingForm pstForm, ModelAndView model) {

    // 商品リスト取得
    EattingForm form = getEattingForm();

    // 食材取得
    Food food = form.getFoodList().get(pstForm.getFood());
    // 料理人取得
    Cook cook = form.getCookList().get(pstForm.getCook());
    // お客取得
    Customer customer = form.getCustomerList().get(pstForm.getCustomer());

    // 食事時間
    int eattingTime = customer.getEatTime(food.getAmount());
    // 金額
    int money = cook.getPrice() + food.getPrice();


    // エラーメッセージ
    String errorMessage = "";
    // オーダー番号
    int maxOrder = 0;
    // オーダー作成
    List<Map<String, Object>> orderMapList = dbService.getOrder();

    if(orderMapList.size() == 0) {
      maxOrder = 0;
    } else {
      System.out.println(orderMapList.get(0).get("no"));
      maxOrder = Integer.parseInt(String.valueOf(orderMapList.get(0).get("no"))) + 1;
    }
    try {
      dbService.insertOrder(food, cook, customer, money, maxOrder);
    } catch (Exception e) {
      errorMessage = "登録に失敗しました。";
    }


    // message作成
    String message = cook.cooking(food.getName()) + "を食べるのに"+ eattingTime + "分かかりました。代金は" + money + "円です。";

    // 表示項目設定
    form.setMessage(message);
    form.setErrorMessage(errorMessage);
    model.getModelMap().addAttribute("eattingForm", form);

    return model;
  }

  @RequestMapping(value = "/Eatting", params="account" , method = RequestMethod.POST)
  public ModelAndView eating(@RequestParam(name = "accountCustomer", required = false) Integer customerNo) {

    // 注文したリストを取得
    List<Map<String, String>> orderList = dbService.getOrderList(customerNo);

    int total = 0;
    // 合計算出
    for (Map<String, String> orderMap: orderList) {
      total +=  Integer.valueOf(orderMap.get("amount"));
    }

    // Viewに渡すデータを設定
    ModelAndView mav = new ModelAndView();
    mav.getModelMap().addAttribute("accountList", orderList);
    mav.getModelMap().addAttribute("total", total);
    mav.setViewName("EatAccount");

    return mav;
  }

  @RequestMapping(value = "/customer", method = RequestMethod.POST)
  public ModelAndView createCustomer(EattingForm form, BindingResult result, Model model) {

    // エラーメッセージ
    String errorMessage = "";

    //画面入力値にエラーがない場合
    if (!result.hasErrors()) {

      Customer customer = form.getInputCustomer();

      //お客さん作成
      try {
        dbService.insertCustomer(customer);
      } catch (DuplicateKeyException e) {
        errorMessage = "受付番号が重複しています。";
      } catch (Exception e) {
        errorMessage = "登録に失敗しました。";
      }
    }

    // フォーム表示項目取得
    EattingForm newForm = getEattingForm();
    newForm.setErrorMessage(errorMessage);
    // Viewに渡すデータを設定
    ModelAndView mav = new ModelAndView();
    mav.getModelMap().addAttribute("eattingForm", newForm);
    mav.setViewName("Eatting");

    return mav;
  }

  private EattingForm getEattingForm() {

    // フォーム初期表示項目を取得する
    form = new EattingForm();
    form.setCustomerList(dbService.getCustomerList());
    form.setCookList(dbService.getCookList());
    form.setFoodList(dbService.getFoodList());
    // ボタン表示設定
    form.setButDisplay();

    return form;
  }
}
