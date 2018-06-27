package jp.co.systena.tigerscave.eatting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jp.co.systena.tigerscave.eatting.model.Cook;
import jp.co.systena.tigerscave.eatting.model.Customer;
import jp.co.systena.tigerscave.eatting.model.FamousCook;
import jp.co.systena.tigerscave.eatting.model.Food;
import jp.co.systena.tigerscave.eatting.model.GeneralCook;
import jp.co.systena.tigerscave.eatting.model.GeneralCustomer;
import jp.co.systena.tigerscave.eatting.model.HungryCustomer;

@Service
public class DbAccessService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Map<String, String>> getOrderList(int customerNo) {

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

  public List<Cook> getCookList() {

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

  public List<Customer> getCustomerList() {

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

  public List<Food> getFoodList() {

    List<Food> foodList = jdbcTemplate.query("SELECT * FROM foodstuff ORDER BY name", new BeanPropertyRowMapper<Food>(Food.class));

    return foodList;
  }

  public void insertCustomer(Customer customer) {

    jdbcTemplate.update(
        "INSERT INTO customer (no,name,type,is_active) VALUES(?,?,?,?)",
        customer.getNo(),
        customer.getName(),
        customer.getType(),
        Boolean.valueOf("TRUE")
      );
  }

  public void insertOrder(Food food, Cook cook, Customer customer, int money, int maxOrder) {

    jdbcTemplate.update(
        "INSERT INTO foodorder (customer_no,food,cook,is_active,amount,no) VALUES(?,?,?,?,?,?)",
        customer.getNo(),
        food.getName(),
        cook.getName(),
        Boolean.valueOf("TRUE"),
        money,
        maxOrder
      );
  }

  public List<Map<String, Object>> getOrder() {
    return jdbcTemplate.queryForList("SELECT no FROM foodorder ORDER BY no DESC");
  }
}
