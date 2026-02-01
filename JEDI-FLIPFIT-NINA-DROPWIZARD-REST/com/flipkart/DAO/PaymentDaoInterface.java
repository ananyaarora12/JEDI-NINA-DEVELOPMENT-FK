package com.flipkart.DAO;

/*import com.flipkart.bean.Customer;
import com.flipkart.bean.Slot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;*/
public interface PaymentDaoInterface {
	
	
	/*public void processPayment(Customer customer, Slot slot);
	public void processRefund(Customer customer, Slot slot);
	 public void bookSlot(Customer customer, Slot slot);
	 public boolean processCardPayment (String cardType);
	  public boolean processUPIPayment();
	  public boolean checkSlotAvailability(Slot slot);*/
	
    public void makePayment(Long cardNumber,String customerEmail);
    
}