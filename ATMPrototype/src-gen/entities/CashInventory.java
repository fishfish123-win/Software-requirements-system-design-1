package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CashInventory implements Serializable {
	
	/* all primary attributes */
	private int Denomination;
	private int Count;
	private LocalDate LastUpdated;
	
	/* all references */
	private List<MaintenanceLog> Update = new LinkedList<MaintenanceLog>(); 
	
	/* all get and set functions */
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
	public LocalDate getLastUpdated() {
		return LastUpdated;
	}	
	
	public void setLastUpdated(LocalDate lastupdated) {
		this.LastUpdated = lastupdated;
	}
	
	/* all functions for reference*/
	public List<MaintenanceLog> getUpdate() {
		return Update;
	}	
	
	public void addUpdate(MaintenanceLog maintenancelog) {
		this.Update.add(maintenancelog);
	}
	
	public void deleteUpdate(MaintenanceLog maintenancelog) {
		this.Update.remove(maintenancelog);
	}
	


}
