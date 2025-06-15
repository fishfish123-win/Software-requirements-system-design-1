package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class DebitCard implements Serializable {
	
	/* all primary attributes */
	private String CardID;
	private String UserID;
	private String AccountNumber;
	private LocalDate ExpiryDate;
	private String PINHash;
	
	/* all references */
	private Account Linked; 
	
	/* all get and set functions */
	public String getCardID() {
		return CardID;
	}	
	
	public void setCardID(String cardid) {
		this.CardID = cardid;
	}
	public String getUserID() {
		return UserID;
	}	
	
	public void setUserID(String userid) {
		this.UserID = userid;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}	
	
	public void setAccountNumber(String accountnumber) {
		this.AccountNumber = accountnumber;
	}
	public LocalDate getExpiryDate() {
		return ExpiryDate;
	}	
	
	public void setExpiryDate(LocalDate expirydate) {
		this.ExpiryDate = expirydate;
	}
	public String getPINHash() {
		return PINHash;
	}	
	
	public void setPINHash(String pinhash) {
		this.PINHash = pinhash;
	}
	
	/* all functions for reference*/
	public Account getLinked() {
		return Linked;
	}	
	
	public void setLinked(Account account) {
		this.Linked = account;
	}			
	


}
