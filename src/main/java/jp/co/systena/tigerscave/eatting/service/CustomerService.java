package jp.co.systena.tigerscave.eatting.service;

import java.util.ArrayList;
import java.util.List;
import jp.co.systena.tigerscave.eatting.model.HungryCustomer;
import jp.co.systena.tigerscave.eatting.model.Customer;
import jp.co.systena.tigerscave.eatting.model.GeneralCustomer;

public class CustomerService {

  private List<Customer> customerList;

  public CustomerService() {
    customerList = new ArrayList<Customer>();
    Customer student = new GeneralCustomer();
    student.setPace(100);
    customerList.add(student);

    Customer champion = new HungryCustomer();
    champion.setPace(300);
    customerList.add(champion);
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

}
