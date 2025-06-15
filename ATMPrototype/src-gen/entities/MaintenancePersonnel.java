package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class MaintenancePersonnel implements Serializable {
	
	/* all primary attributes */
	private String OperatorID;
	private String Name;
	private String KeyID;
	private String AuthCredentials;
	
	/* all references */
	private List<MaintenanceLog> Performs = new LinkedList<MaintenanceLog>(); 
	
	/* all get and set functions */
	public String getOperatorID() {
		return OperatorID;
	}	
	
	public void setOperatorID(String operatorid) {
		this.OperatorID = operatorid;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public String getKeyID() {
		return KeyID;
	}	
	
	public void setKeyID(String keyid) {
		this.KeyID = keyid;
	}
	public String getAuthCredentials() {
		return AuthCredentials;
	}	
	
	public void setAuthCredentials(String authcredentials) {
		this.AuthCredentials = authcredentials;
	}
	
	/* all functions for reference*/
	public List<MaintenanceLog> getPerforms() {
		return Performs;
	}	
	
	public void addPerforms(MaintenanceLog maintenancelog) {
		this.Performs.add(maintenancelog);
	}
	
	public void deletePerforms(MaintenanceLog maintenancelog) {
		this.Performs.remove(maintenancelog);
	}
	


}
