package jp.co.systena.tigerscave.eatting.model;

public class Food extends FoodBase{

  public void setName(String name) {
    super.name = name;
  }

  public String getName() {

    return name;
  }

  public void setPrice(int price) {
    super.price = price;
  }

  public int getPrice() {

    return price;
  }

  public void setAmount(int amount) {
    super.amount = amount;
  }

  public int getAmount() {

    return amount;
  }

  public void setIsActive(Boolean isActive) {
    super.isActive = isActive;
  }

  public Boolean getIsActive() {

    return isActive;
  }
}
