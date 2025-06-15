package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<User> UserInstances = new LinkedList<User>();
	private static List<DebitCard> DebitCardInstances = new LinkedList<DebitCard>();
	private static List<Account> AccountInstances = new LinkedList<Account>();
	private static List<Transaction> TransactionInstances = new LinkedList<Transaction>();
	private static List<CashInventory> CashInventoryInstances = new LinkedList<CashInventory>();
	private static List<MaintenancePersonnel> MaintenancePersonnelInstances = new LinkedList<MaintenancePersonnel>();
	private static List<MaintenanceLog> MaintenanceLogInstances = new LinkedList<MaintenanceLog>();
	private static List<SecurityToken> SecurityTokenInstances = new LinkedList<SecurityToken>();
	private static List<AuditLog> AuditLogInstances = new LinkedList<AuditLog>();
	private static List<TransferRecord> TransferRecordInstances = new LinkedList<TransferRecord>();
	private static List<BalanceInquiry> BalanceInquiryInstances = new LinkedList<BalanceInquiry>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("User", UserInstances);
		AllInstance.put("DebitCard", DebitCardInstances);
		AllInstance.put("Account", AccountInstances);
		AllInstance.put("Transaction", TransactionInstances);
		AllInstance.put("CashInventory", CashInventoryInstances);
		AllInstance.put("MaintenancePersonnel", MaintenancePersonnelInstances);
		AllInstance.put("MaintenanceLog", MaintenanceLogInstances);
		AllInstance.put("SecurityToken", SecurityTokenInstances);
		AllInstance.put("AuditLog", AuditLogInstances);
		AllInstance.put("TransferRecord", TransferRecordInstances);
		AllInstance.put("BalanceInquiry", BalanceInquiryInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(UserInstances);
			stateSave.writeObject(DebitCardInstances);
			stateSave.writeObject(AccountInstances);
			stateSave.writeObject(TransactionInstances);
			stateSave.writeObject(CashInventoryInstances);
			stateSave.writeObject(MaintenancePersonnelInstances);
			stateSave.writeObject(MaintenanceLogInstances);
			stateSave.writeObject(SecurityTokenInstances);
			stateSave.writeObject(AuditLogInstances);
			stateSave.writeObject(TransferRecordInstances);
			stateSave.writeObject(BalanceInquiryInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				UserInstances =  (List<User>) stateLoad.readObject();
				AllInstance.put("User", UserInstances);
				DebitCardInstances =  (List<DebitCard>) stateLoad.readObject();
				AllInstance.put("DebitCard", DebitCardInstances);
				AccountInstances =  (List<Account>) stateLoad.readObject();
				AllInstance.put("Account", AccountInstances);
				TransactionInstances =  (List<Transaction>) stateLoad.readObject();
				AllInstance.put("Transaction", TransactionInstances);
				CashInventoryInstances =  (List<CashInventory>) stateLoad.readObject();
				AllInstance.put("CashInventory", CashInventoryInstances);
				MaintenancePersonnelInstances =  (List<MaintenancePersonnel>) stateLoad.readObject();
				AllInstance.put("MaintenancePersonnel", MaintenancePersonnelInstances);
				MaintenanceLogInstances =  (List<MaintenanceLog>) stateLoad.readObject();
				AllInstance.put("MaintenanceLog", MaintenanceLogInstances);
				SecurityTokenInstances =  (List<SecurityToken>) stateLoad.readObject();
				AllInstance.put("SecurityToken", SecurityTokenInstances);
				AuditLogInstances =  (List<AuditLog>) stateLoad.readObject();
				AllInstance.put("AuditLog", AuditLogInstances);
				TransferRecordInstances =  (List<TransferRecord>) stateLoad.readObject();
				AllInstance.put("TransferRecord", TransferRecordInstances);
				BalanceInquiryInstances =  (List<BalanceInquiry>) stateLoad.readObject();
				AllInstance.put("BalanceInquiry", BalanceInquiryInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static User createUserObject() {
		User o = new User();
		return o;
	}
	
	public static boolean addUserObject(User o) {
		return UserInstances.add(o);
	}
	
	public static boolean addUserObjects(List<User> os) {
		return UserInstances.addAll(os);
	}
	
	public static boolean deleteUserObject(User o) {
		return UserInstances.remove(o);
	}
	
	public static boolean deleteUserObjects(List<User> os) {
		return UserInstances.removeAll(os);
	}
	public static DebitCard createDebitCardObject() {
		DebitCard o = new DebitCard();
		return o;
	}
	
	public static boolean addDebitCardObject(DebitCard o) {
		return DebitCardInstances.add(o);
	}
	
	public static boolean addDebitCardObjects(List<DebitCard> os) {
		return DebitCardInstances.addAll(os);
	}
	
	public static boolean deleteDebitCardObject(DebitCard o) {
		return DebitCardInstances.remove(o);
	}
	
	public static boolean deleteDebitCardObjects(List<DebitCard> os) {
		return DebitCardInstances.removeAll(os);
	}
	public static Account createAccountObject() {
		Account o = new Account();
		return o;
	}
	
	public static boolean addAccountObject(Account o) {
		return AccountInstances.add(o);
	}
	
	public static boolean addAccountObjects(List<Account> os) {
		return AccountInstances.addAll(os);
	}
	
	public static boolean deleteAccountObject(Account o) {
		return AccountInstances.remove(o);
	}
	
	public static boolean deleteAccountObjects(List<Account> os) {
		return AccountInstances.removeAll(os);
	}
	public static Transaction createTransactionObject() {
		Transaction o = new Transaction();
		return o;
	}
	
	public static boolean addTransactionObject(Transaction o) {
		return TransactionInstances.add(o);
	}
	
	public static boolean addTransactionObjects(List<Transaction> os) {
		return TransactionInstances.addAll(os);
	}
	
	public static boolean deleteTransactionObject(Transaction o) {
		return TransactionInstances.remove(o);
	}
	
	public static boolean deleteTransactionObjects(List<Transaction> os) {
		return TransactionInstances.removeAll(os);
	}
	public static CashInventory createCashInventoryObject() {
		CashInventory o = new CashInventory();
		return o;
	}
	
	public static boolean addCashInventoryObject(CashInventory o) {
		return CashInventoryInstances.add(o);
	}
	
	public static boolean addCashInventoryObjects(List<CashInventory> os) {
		return CashInventoryInstances.addAll(os);
	}
	
	public static boolean deleteCashInventoryObject(CashInventory o) {
		return CashInventoryInstances.remove(o);
	}
	
	public static boolean deleteCashInventoryObjects(List<CashInventory> os) {
		return CashInventoryInstances.removeAll(os);
	}
	public static MaintenancePersonnel createMaintenancePersonnelObject() {
		MaintenancePersonnel o = new MaintenancePersonnel();
		return o;
	}
	
	public static boolean addMaintenancePersonnelObject(MaintenancePersonnel o) {
		return MaintenancePersonnelInstances.add(o);
	}
	
	public static boolean addMaintenancePersonnelObjects(List<MaintenancePersonnel> os) {
		return MaintenancePersonnelInstances.addAll(os);
	}
	
	public static boolean deleteMaintenancePersonnelObject(MaintenancePersonnel o) {
		return MaintenancePersonnelInstances.remove(o);
	}
	
	public static boolean deleteMaintenancePersonnelObjects(List<MaintenancePersonnel> os) {
		return MaintenancePersonnelInstances.removeAll(os);
	}
	public static MaintenanceLog createMaintenanceLogObject() {
		MaintenanceLog o = new MaintenanceLog();
		return o;
	}
	
	public static boolean addMaintenanceLogObject(MaintenanceLog o) {
		return MaintenanceLogInstances.add(o);
	}
	
	public static boolean addMaintenanceLogObjects(List<MaintenanceLog> os) {
		return MaintenanceLogInstances.addAll(os);
	}
	
	public static boolean deleteMaintenanceLogObject(MaintenanceLog o) {
		return MaintenanceLogInstances.remove(o);
	}
	
	public static boolean deleteMaintenanceLogObjects(List<MaintenanceLog> os) {
		return MaintenanceLogInstances.removeAll(os);
	}
	public static SecurityToken createSecurityTokenObject() {
		SecurityToken o = new SecurityToken();
		return o;
	}
	
	public static boolean addSecurityTokenObject(SecurityToken o) {
		return SecurityTokenInstances.add(o);
	}
	
	public static boolean addSecurityTokenObjects(List<SecurityToken> os) {
		return SecurityTokenInstances.addAll(os);
	}
	
	public static boolean deleteSecurityTokenObject(SecurityToken o) {
		return SecurityTokenInstances.remove(o);
	}
	
	public static boolean deleteSecurityTokenObjects(List<SecurityToken> os) {
		return SecurityTokenInstances.removeAll(os);
	}
	public static AuditLog createAuditLogObject() {
		AuditLog o = new AuditLog();
		return o;
	}
	
	public static boolean addAuditLogObject(AuditLog o) {
		return AuditLogInstances.add(o);
	}
	
	public static boolean addAuditLogObjects(List<AuditLog> os) {
		return AuditLogInstances.addAll(os);
	}
	
	public static boolean deleteAuditLogObject(AuditLog o) {
		return AuditLogInstances.remove(o);
	}
	
	public static boolean deleteAuditLogObjects(List<AuditLog> os) {
		return AuditLogInstances.removeAll(os);
	}
	public static TransferRecord createTransferRecordObject() {
		TransferRecord o = new TransferRecord();
		return o;
	}
	
	public static boolean addTransferRecordObject(TransferRecord o) {
		return TransferRecordInstances.add(o);
	}
	
	public static boolean addTransferRecordObjects(List<TransferRecord> os) {
		return TransferRecordInstances.addAll(os);
	}
	
	public static boolean deleteTransferRecordObject(TransferRecord o) {
		return TransferRecordInstances.remove(o);
	}
	
	public static boolean deleteTransferRecordObjects(List<TransferRecord> os) {
		return TransferRecordInstances.removeAll(os);
	}
	public static BalanceInquiry createBalanceInquiryObject() {
		BalanceInquiry o = new BalanceInquiry();
		return o;
	}
	
	public static boolean addBalanceInquiryObject(BalanceInquiry o) {
		return BalanceInquiryInstances.add(o);
	}
	
	public static boolean addBalanceInquiryObjects(List<BalanceInquiry> os) {
		return BalanceInquiryInstances.addAll(os);
	}
	
	public static boolean deleteBalanceInquiryObject(BalanceInquiry o) {
		return BalanceInquiryInstances.remove(o);
	}
	
	public static boolean deleteBalanceInquiryObjects(List<BalanceInquiry> os) {
		return BalanceInquiryInstances.removeAll(os);
	}
  
}

