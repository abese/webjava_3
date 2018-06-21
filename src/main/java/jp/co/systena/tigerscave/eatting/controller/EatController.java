package jp.co.systena.tigerscave.eatting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
import jp.co.systena.tigerscave.eatting.model.FamousCook;
import jp.co.systena.tigerscave.eatting.model.Food;
import jp.co.systena.tigerscave.eatting.model.GeneralCook;
import jp.co.systena.tigerscave.eatting.model.GeneralCustomer;
import jp.co.systena.tigerscave.eatting.model.HungryCustomer;

@Controller
public class EatController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  // 表示用クラス
  EattingForm form;

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
    int eattingTime = customer.getEattime(food.getAmount());
    // 金額
    int money = cook.getPrice() + food.getPrice();


    // エラーメッセージ
    String errorMessage = "";
    // オーダー番号
    int maxOrder = 0;
    // オーダー作成
    List<Map<String, Object>> orderMapList = jdbcTemplate.queryForList("SELECT no FROM foodorder ORDER BY no DESC");

    if(orderMapList.size() == 0) {
      maxOrder = 0;
    } else {
      System.out.println(orderMapList.get(0).get("no"));
      maxOrder = Integer.parseInt(String.valueOf(orderMapList.get(0).get("no"))) + 1;
    }
    try {
      int insertCount = jdbcTemplate.update(
            "INSERT INTO foodorder (customer_no,food,cook,is_active,amount,no) VALUES(?,?,?,?,?,?)",
            customer.getNo(),
            food.getName(),
            cook.getName(),
            Boolean.valueOf("TRUE"),
            money,
            maxOrder
          );
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
    List<Map<String, String>> orderList = getOrderList(customerNo);

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
        int insertCount = jdbcTemplate.update(
              "INSERT INTO customer (no,name,type,is_active) VALUES(?,?,?,?)",
              customer.getNo(),
              customer.getName(),
              customer.getType(),
              Boolean.valueOf("TRUE")
            );
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



  private List<Food> getFoodList() {

    List<Food> foodList = jdbcTemplate.query("SELECT * FROM foodstuff ORDER BY name", new BeanPropertyRowMapper<Food>(Food.class));

    return foodList;
  }

  private List<Customer> getCustomerList() {

    // お客一覧取得
    List<Map<String, Object>> customerMapList = jdbcTemplate.queryForList(("SELECT * FROM customer WHERE is_active = true ORDER BY name"));

    List<Customer> customerList = new ArrayList<>();

    // 取得した情報を表示用のListに格納
    for (Map<String, Object> customerMap : customerMapList) {

      // お客さんのタイプによってインスタンスを変える
      Customer customer;
      if ("high".equals(customerMap.get("type"))) {
        customer = new HungryCustomer();
      } else if("row".equals(customerMap.get("type"))) {
        customer = new GeneralCustomer();
      } else {
        continue;
      }
      customer.setName((String)customerMap.get("name"));
      customer.setNo((String)customerMap.get("no"));
      customerList.add(customer);
    }

    return customerList;
  }


  private List<Cook> getCookList() {

    // 料理人一覧取得
    List<Map<String, Object>> cookMapList = jdbcTemplate.queryForList("SELECT * FROM cook ORDER BY name");

    List<Cook> cookList = new ArrayList<>();

    // 取得した情報を表示用のListに格納
    for (Map<String, Object> cookMap : cookMapList) {

      // 種別によりインスタンスを変える
      Cook cook;
      if ("high".equals(cookMap.get("type"))) {
        cook = new FamousCook();
      } else if("row".equals(cookMap.get("type"))) {
        cook = new GeneralCook();
      } else {
        continue;
      }
      cook.setCookingName((String)cookMap.get("cooking_name"));
      cook.setName((String)cookMap.get("name"));
      cook.setPrice((int) cookMap.get("price"));
      cookList.add(cook);
    }

    return cookList;
  }

  private List<Map<String, String>> getOrderList(int customerNo) {

    // お客さんの全オーダー取得
    List<Map<String, Object>> orderMapList = jdbcTemplate.queryForList(("SELECT * FROM foodorder WHERE customer_no = ? ORDER BY no"), String.valueOf(customerNo));

    // 表示用List
    List<Map<String, String>> orderList = new ArrayList<>();

    // 表示用Map格納
    for (Map<String, Object> orderMap : orderMapList) {

      Map<String, String> order = new HashMap<>();

      order.put("no", (String)orderMap.get("customer_no"));
      order.put("food", (String)orderMap.get("food"));
      order.put("cook", (String)orderMap.get("cook"));
      order.put("amount", String.valueOf(orderMap.get("amount")));

      orderList.add(order);
    }

    // お客さんを無効化
    jdbcTemplate.update("UPDATE customer SET is_active = false WHERE no = ?", String.valueOf(customerNo));


    return orderList;
  }

  private EattingForm getEattingForm() {

    // フォーム初期表示項目を取得する
    form = new EattingForm();
    form.setCustomerList(getCustomerList());
    form.setCookList(getCookList());
    form.setFoodList(getFoodList());
    // ボタン表示設定
    form.setButDisplay();

    return form;
  }
}
