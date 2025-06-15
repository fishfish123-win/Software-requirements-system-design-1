package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class User implements Serializable {
	
	/* all primary attributes */
	private String UserID;
	private String Name;
	private String Role;
	
	/* all references */
	private List<DebitCard> Own = new LinkedList<DebitCard>(); 
	private List<Account> Holds = new LinkedList<Account>(); 
	private List<AuditLog> Generates = new LinkedList<AuditLog>(); 
	
	/* all get and set functions */
	public String getUserID() {
		return UserID;
	}	
	
	public void setUserID(String userid) {
		this.UserID = userid;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public String getRole() {
		return Role;
	}	
	
	public void setRole(String role) {
		this.Role = role;
	}
	
	/* all functions for reference*/
	public List<DebitCard> getOwn() {
		return Own;
	}	
	
	public void addOwn(DebitCard debitcard) {
		this.Own.add(debitcard);
	}
	
	public void deleteOwn(DebitCard debitcard) {
		this.Own.remove(debitcard);
	}
	public List<Account> getHolds() {
		return Holds;
	}	
	
	public void addHolds(Account account) {
		this.Holds.add(account);
	}
	
	public void deleteHolds(Account account) {
		this.Holds.remove(account);
	}
	public List<AuditLog> getGenerates() {
		return Generates;
	}	
	
	public void addGenerates(AuditLog auditlog) {
		this.Generates.add(auditlog);
	}
	
	public void deleteGenerates(AuditLog auditlog) {
		this.Generates.remove(auditlog);
	}
	


}
