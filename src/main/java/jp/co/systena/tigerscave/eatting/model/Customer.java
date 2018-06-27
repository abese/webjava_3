package jp.co.systena.tigerscave.eatting.model;

public abstract class Customer {

  private String no;
  private String name;
  private String type;
  private int totalAmount;
  private int pace;

  public String getNo() {
    return no;
  }
  public void setNo(String no) {
    this.no = no;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public int getTotalAmount() {
    return totalAmount;
  }
  public void setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
  }
  public int getPace() {
    return pace;
  }
  public void setPace(int pace) {
    this.pace = pace;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public abstract int getEatTime(int amount);
}
