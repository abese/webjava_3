package jp.co.systena.tigerscave.eatting.model;

public class GeneralCustomer extends Customer {

  private int pace = 100;

  public GeneralCustomer() {
  }
  @Override
  public int getEattime(int amount) {

    return amount / pace;
  }
}
