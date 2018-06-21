package jp.co.systena.tigerscave.eatting.model;

public abstract class Cook {

  private int price;
  private String name;
  private String cookingName;

  public void setPrice(int price) {
    this.price = price;
  }

  public int getPrice() {
    return this.price;
  }

  public String getCookingName() {
    return cookingName;
  }

  public void setCookingName(String cookingName) {
    this.cookingName = cookingName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public abstract String cooking(String foodstuff);
}
