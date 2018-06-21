package jp.co.systena.tigerscave.eatting.model;

import java.util.List;

public class EattingForm {

  // 表示項目リスト
  private List<Cook> cookList;
  private List<Customer> customerList;
  private List<Food> foodList;

  // 入力項目
  private int cook;
  private int customer;
  private int food;
  private Customer inputCustomer = new InputCustomer();

  // ボタン表示フラグ
  private Boolean isButDisplay;

  // フォーム表示メッセージ
  private String message;
  private String errorMessage;


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Cook> getCookList() {
    return cookList;
  }

  public void setCookList(List<Cook> cookList) {
    this.cookList = cookList;
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

  public void setCustomerList(List<Customer> customerList) {
    this.customerList = customerList;
  }

  public List<Food> getFoodList() {
    return foodList;
  }

  public void setFoodList(List<Food> foodList) {
    this.foodList = foodList;
  }

  public Customer getInputCustomer() {
    return inputCustomer;
  }

  public void setInputCustomer(Customer inputCustomer) {
    this.inputCustomer = inputCustomer;
  }

  public Boolean getIsButDisplay() {
    return isButDisplay;
  }

  public void setIsButDisplay(Boolean isButDisplay) {
    this.isButDisplay = isButDisplay;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getCook() {
    return cook;
  }

  public void setCook(int cook) {
    this.cook = cook;
  }

  public int getCustomer() {
    return customer;
  }

  public void setCustomer(int customer) {
    this.customer = customer;
  }

  public int getFood() {
    return food;
  }

  public void setFood(int food) {
    this.food = food;
  }

  public void setButDisplay() {

    isButDisplay = true;

    // 表示Listが空の場合ボタンを表示しない
    if(cookList.size() == 0 || customerList.size() == 0 || foodList.size() == 0) {
      isButDisplay = false;
    }

  }
}
