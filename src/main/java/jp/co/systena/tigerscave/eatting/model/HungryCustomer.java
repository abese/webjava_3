package jp.co.systena.tigerscave.eatting.model;

public class HungryCustomer extends Customer {

  private int pace = 300;

  public HungryCustomer() {
  }

  @Override
  public int getEattime(int amount) {

    return amount / pace;
  }

}
