package jp.co.systena.tigerscave.eatting.model;

public class GeneralCook extends Cook{

  private String habit = "よくある";

  @Override
  public String cooking(String foodstuff) {

    return habit + foodstuff + getCookingName();
  }

}
