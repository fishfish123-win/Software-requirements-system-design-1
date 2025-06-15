package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SecurityToken implements Serializable {
	
	/* all primary attributes */
	private String TokenID;
	private String Type;
	private LocalDate ExpiryDate;
	
	/* all references */
	private List<AuditLog> Secures = new LinkedList<AuditLog>(); 
	
	/* all get and set functions */
	public String getTokenID() {
		return TokenID;
	}	
	
	public void setTokenID(String tokenid) {
		this.TokenID = tokenid;
	}
	public String getType() {
		return Type;
	}	
	
	public void setType(String type) {
		this.Type = type;
	}
	public LocalDate getExpiryDate() {
		return ExpiryDate;
	}	
	
	public void setExpiryDate(LocalDate expirydate) {
		this.ExpiryDate = expirydate;
	}
	
	/* all functions for reference*/
	public List<AuditLog> getSecures() {
		return Secures;
	}	
	
	public void addSecures(AuditLog auditlog) {
		this.Secures.add(auditlog);
	}
	
	public void deleteSecures(AuditLog auditlog) {
		this.Secures.remove(auditlog);
	}
	


}
