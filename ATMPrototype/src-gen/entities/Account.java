package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Account implements Serializable {
	
	/* all primary attributes */
	private String AccountNumber;
	private String UserID;
	private float Balance;
	private float FrozenBalance;
	
	/* all references */
	private List<Transaction> Has = new LinkedList<Transaction>(); 
	private List<BalanceInquiry> Have = new LinkedList<BalanceInquiry>(); 
	private List<TransferRecord> Generates = new LinkedList<TransferRecord>(); 
	
	/* all get and set functions */
	public String getAccountNumber() {
		return AccountNumber;
	}	
	
	public void setAccountNumber(String accountnumber) {
		this.AccountNumber = accountnumber;
	}
	public String getUserID() {
		return UserID;
	}	
	
	public void setUserID(String userid) {
		this.UserID = userid;
	}
	public float getBalance() {
		return Balance;
	}	
	
	public void setBalance(float balance) {
		this.Balance = balance;
	}
	public float getFrozenBalance() {
		return FrozenBalance;
	}	
	
	public void setFrozenBalance(float frozenbalance) {
		this.FrozenBalance = frozenbalance;
	}
	
	/* all functions for reference*/
	public List<Transaction> getHas() {
		return Has;
	}	
	
	public void addHas(Transaction transaction) {
		this.Has.add(transaction);
	}
	
	public void deleteHas(Transaction transaction) {
		this.Has.remove(transaction);
	}
	public List<BalanceInquiry> getHave() {
		return Have;
	}	
	
	public void addHave(BalanceInquiry balanceinquiry) {
		this.Have.add(balanceinquiry);
	}
	
	public void deleteHave(BalanceInquiry balanceinquiry) {
		this.Have.remove(balanceinquiry);
	}
	public List<TransferRecord> getGenerates() {
		return Generates;
	}	
	
	public void addGenerates(TransferRecord transferrecord) {
		this.Generates.add(transferrecord);
	}
	
	public void deleteGenerates(TransferRecord transferrecord) {
		this.Generates.remove(transferrecord);
	}
	


}
