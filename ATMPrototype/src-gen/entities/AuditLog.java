package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class AuditLog implements Serializable {
	
	/* all primary attributes */
	private String LogID;
	private String OperationType;
	private LocalDate Timestamp;
	private String UserID;
	private String Details;
	
	/* all references */
	
	/* all get and set functions */
	public String getLogID() {
		return LogID;
	}	
	
	public void setLogID(String logid) {
		this.LogID = logid;
	}
	public String getOperationType() {
		return OperationType;
	}	
	
	public void setOperationType(String operationtype) {
		this.OperationType = operationtype;
	}
	public LocalDate getTimestamp() {
		return Timestamp;
	}	
	
	public void setTimestamp(LocalDate timestamp) {
		this.Timestamp = timestamp;
	}
	public String getUserID() {
		return UserID;
	}	
	
	public void setUserID(String userid) {
		this.UserID = userid;
	}
	public String getDetails() {
		return Details;
	}	
	
	public void setDetails(String details) {
		this.Details = details;
	}
	
	/* all functions for reference*/
	


}
