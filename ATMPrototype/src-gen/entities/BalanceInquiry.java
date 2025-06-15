package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class BalanceInquiry implements Serializable {
	
	/* all primary attributes */
	private String InquiryID;
	private String AccountNumber;
	private LocalDate Timestamp;
	private String Result;
	
	/* all references */
	
	/* all get and set functions */
	public String getInquiryID() {
		return InquiryID;
	}	
	
	public void setInquiryID(String inquiryid) {
		this.InquiryID = inquiryid;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}	
	
	public void setAccountNumber(String accountnumber) {
		this.AccountNumber = accountnumber;
	}
	public LocalDate getTimestamp() {
		return Timestamp;
	}	
	
	public void setTimestamp(LocalDate timestamp) {
		this.Timestamp = timestamp;
	}
	public String getResult() {
		return Result;
	}	
	
	public void setResult(String result) {
		this.Result = result;
	}
	
	/* all functions for reference*/
	


}
