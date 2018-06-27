package jp.co.systena.tigerscave.eatting.model;

public class HungryCustomer extends Customer {

  private int pace = 300;

  public HungryCustomer() {
  }

  @Override
  public int getEatTime(int amount) {

    return amount / pace;
  }

}
