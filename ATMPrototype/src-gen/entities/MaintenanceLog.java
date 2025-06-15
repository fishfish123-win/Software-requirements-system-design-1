package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class MaintenanceLog implements Serializable {
	
	/* all primary attributes */
	private String LogID;
	private String OperatorID;
	private int Denomination;
	private int Count;
	private LocalDate Timestamp;
	
	/* all references */
	
	/* all get and set functions */
	public String getLogID() {
		return LogID;
	}	
	
	public void setLogID(String logid) {
		this.LogID = logid;
	}
	public String getOperatorID() {
		return OperatorID;
	}	
	
	public void setOperatorID(String operatorid) {
		this.OperatorID = operatorid;
	}
	public int getDenomination() {
		return Denomination;
	}	
	
	public void setDenomination(int denomination) {
		this.Denomination = denomination;
	}
	public int getCount() {
		return Count;
	}	
	
	public void setCount(int count) {
		this.Count = count;
	}
	public LocalDate getTimestamp() {
		return Timestamp;
	}	
	
	public void setTimestamp(LocalDate timestamp) {
		this.Timestamp = timestamp;
	}
	
	/* all functions for reference*/
	


}
