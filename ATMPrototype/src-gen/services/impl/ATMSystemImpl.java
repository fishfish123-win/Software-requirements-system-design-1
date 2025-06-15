package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class ATMSystemImpl implements ATMSystem, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ATMSystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

	public void refresh() {
		WithdrawCashService withdrawcashservice_service = (WithdrawCashService) ServiceManager
				.getAllInstancesOf("WithdrawCashService").get(0);
		CheckBalanceService checkbalanceservice_service = (CheckBalanceService) ServiceManager
				.getAllInstancesOf("CheckBalanceService").get(0);
		ReplenishCashService replenishcashservice_service = (ReplenishCashService) ServiceManager
				.getAllInstancesOf("ReplenishCashService").get(0);
		ManageAuditLogsService manageauditlogsservice_service = (ManageAuditLogsService) ServiceManager
				.getAllInstancesOf("ManageAuditLogsService").get(0);
		TransferFundsService transferfundsservice_service = (TransferFundsService) ServiceManager
				.getAllInstancesOf("TransferFundsService").get(0);
		ChangePINService changepinservice_service = (ChangePINService) ServiceManager
				.getAllInstancesOf("ChangePINService").get(0);
		UpdateFirmwareService updatefirmwareservice_service = (UpdateFirmwareService) ServiceManager
				.getAllInstancesOf("UpdateFirmwareService").get(0);
		VerifyPINService verifypinservice_service = (VerifyPINService) ServiceManager
				.getAllInstancesOf("VerifyPINService").get(0);
		DisplayHistoryService displayhistoryservice_service = (DisplayHistoryService) ServiceManager
				.getAllInstancesOf("DisplayHistoryService").get(0);
		SecurityAuthenticationService securityauthenticationservice_service = (SecurityAuthenticationService) ServiceManager
				.getAllInstancesOf("SecurityAuthenticationService").get(0);
		SetTransactionLimitsService settransactionlimitsservice_service = (SetTransactionLimitsService) ServiceManager
				.getAllInstancesOf("SetTransactionLimitsService").get(0);
	}			
	
	/* Generate buiness logic according to functional requirement */
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
