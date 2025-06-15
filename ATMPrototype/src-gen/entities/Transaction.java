package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Transaction implements Serializable {
	
	/* all primary attributes */
	private String TransactionID;
	private String AccountNumber;
	private float Amount;
	private LocalDate Timestamp;
	private String TransactionType;
	
	/* all references */
	
	/* all get and set functions */
	public String getTransactionID() {
		return TransactionID;
	}	
	
	public void setTransactionID(String transactionid) {
		this.TransactionID = transactionid;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}	
	
	public void setAccountNumber(String accountnumber) {
		this.AccountNumber = accountnumber;
	}
	public float getAmount() {
		return Amount;
	}	
	
	public void setAmount(float amount) {
		this.Amount = amount;
	}
	public LocalDate getTimestamp() {
		return Timestamp;
	}	
	
	public void setTimestamp(LocalDate timestamp) {
		this.Timestamp = timestamp;
	}
	public String getTransactionType() {
		return TransactionType;
	}	
	
	public void setTransactionType(String transactiontype) {
		this.TransactionType = transactiontype;
	}
	
	/* all functions for reference*/
	


}
