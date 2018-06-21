package jp.co.systena.tigerscave.eatting.model;

public class FamousCook extends Cook {

  private String habit = "とってもおいしい";

  @Override
  public String cooking(String foodstuff) {

    return habit + foodstuff + getCookingName();
  }

}
