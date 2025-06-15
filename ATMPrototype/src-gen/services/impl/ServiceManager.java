package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<ATMSystem> ATMSystemInstances = new LinkedList<ATMSystem>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	private static List<WithdrawCashService> WithdrawCashServiceInstances = new LinkedList<WithdrawCashService>();
	private static List<CheckBalanceService> CheckBalanceServiceInstances = new LinkedList<CheckBalanceService>();
	private static List<ReplenishCashService> ReplenishCashServiceInstances = new LinkedList<ReplenishCashService>();
	private static List<ManageAuditLogsService> ManageAuditLogsServiceInstances = new LinkedList<ManageAuditLogsService>();
	private static List<TransferFundsService> TransferFundsServiceInstances = new LinkedList<TransferFundsService>();
	private static List<ChangePINService> ChangePINServiceInstances = new LinkedList<ChangePINService>();
	private static List<UpdateFirmwareService> UpdateFirmwareServiceInstances = new LinkedList<UpdateFirmwareService>();
	private static List<VerifyPINService> VerifyPINServiceInstances = new LinkedList<VerifyPINService>();
	private static List<DisplayHistoryService> DisplayHistoryServiceInstances = new LinkedList<DisplayHistoryService>();
	private static List<SecurityAuthenticationService> SecurityAuthenticationServiceInstances = new LinkedList<SecurityAuthenticationService>();
	private static List<SetTransactionLimitsService> SetTransactionLimitsServiceInstances = new LinkedList<SetTransactionLimitsService>();
	
	static {
		AllServiceInstance.put("ATMSystem", ATMSystemInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
		AllServiceInstance.put("WithdrawCashService", WithdrawCashServiceInstances);
		AllServiceInstance.put("CheckBalanceService", CheckBalanceServiceInstances);
		AllServiceInstance.put("ReplenishCashService", ReplenishCashServiceInstances);
		AllServiceInstance.put("ManageAuditLogsService", ManageAuditLogsServiceInstances);
		AllServiceInstance.put("TransferFundsService", TransferFundsServiceInstances);
		AllServiceInstance.put("ChangePINService", ChangePINServiceInstances);
		AllServiceInstance.put("UpdateFirmwareService", UpdateFirmwareServiceInstances);
		AllServiceInstance.put("VerifyPINService", VerifyPINServiceInstances);
		AllServiceInstance.put("DisplayHistoryService", DisplayHistoryServiceInstances);
		AllServiceInstance.put("SecurityAuthenticationService", SecurityAuthenticationServiceInstances);
		AllServiceInstance.put("SetTransactionLimitsService", SetTransactionLimitsServiceInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static ATMSystem createATMSystem() {
		ATMSystem s = new ATMSystemImpl();
		ATMSystemInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
	public static WithdrawCashService createWithdrawCashService() {
		WithdrawCashService s = new WithdrawCashServiceImpl();
		WithdrawCashServiceInstances.add(s);
		return s;
	}
	public static CheckBalanceService createCheckBalanceService() {
		CheckBalanceService s = new CheckBalanceServiceImpl();
		CheckBalanceServiceInstances.add(s);
		return s;
	}
	public static ReplenishCashService createReplenishCashService() {
		ReplenishCashService s = new ReplenishCashServiceImpl();
		ReplenishCashServiceInstances.add(s);
		return s;
	}
	public static ManageAuditLogsService createManageAuditLogsService() {
		ManageAuditLogsService s = new ManageAuditLogsServiceImpl();
		ManageAuditLogsServiceInstances.add(s);
		return s;
	}
	public static TransferFundsService createTransferFundsService() {
		TransferFundsService s = new TransferFundsServiceImpl();
		TransferFundsServiceInstances.add(s);
		return s;
	}
	public static ChangePINService createChangePINService() {
		ChangePINService s = new ChangePINServiceImpl();
		ChangePINServiceInstances.add(s);
		return s;
	}
	public static UpdateFirmwareService createUpdateFirmwareService() {
		UpdateFirmwareService s = new UpdateFirmwareServiceImpl();
		UpdateFirmwareServiceInstances.add(s);
		return s;
	}
	public static VerifyPINService createVerifyPINService() {
		VerifyPINService s = new VerifyPINServiceImpl();
		VerifyPINServiceInstances.add(s);
		return s;
	}
	public static DisplayHistoryService createDisplayHistoryService() {
		DisplayHistoryService s = new DisplayHistoryServiceImpl();
		DisplayHistoryServiceInstances.add(s);
		return s;
	}
	public static SecurityAuthenticationService createSecurityAuthenticationService() {
		SecurityAuthenticationService s = new SecurityAuthenticationServiceImpl();
		SecurityAuthenticationServiceInstances.add(s);
		return s;
	}
	public static SetTransactionLimitsService createSetTransactionLimitsService() {
		SetTransactionLimitsService s = new SetTransactionLimitsServiceImpl();
		SetTransactionLimitsServiceInstances.add(s);
		return s;
	}
}	
