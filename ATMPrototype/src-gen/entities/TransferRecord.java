package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class TransferRecord implements Serializable {
	
	/* all primary attributes */
	private String TransferID;
	private String SenderAccount;
	private String ReceiverAccount;
	private float Amount;
	private String Status;
	
	/* all references */
	
	/* all get and set functions */
	public String getTransferID() {
		return TransferID;
	}	
	
	public void setTransferID(String transferid) {
		this.TransferID = transferid;
	}
	public String getSenderAccount() {
		return SenderAccount;
	}	
	
	public void setSenderAccount(String senderaccount) {
		this.SenderAccount = senderaccount;
	}
	public String getReceiverAccount() {
		return ReceiverAccount;
	}	
	
	public void setReceiverAccount(String receiveraccount) {
		this.ReceiverAccount = receiveraccount;
	}
	public float getAmount() {
		return Amount;
	}	
	
	public void setAmount(float amount) {
		this.Amount = amount;
	}
	public String getStatus() {
		return Status;
	}	
	
	public void setStatus(String status) {
		this.Status = status;
	}
	
	/* all functions for reference*/
	


}
