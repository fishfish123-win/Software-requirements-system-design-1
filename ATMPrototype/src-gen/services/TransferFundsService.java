package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface TransferFundsService {

	/* all system operations of the use case*/
	boolean selectTransferMenu(String optionID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean chooseTargetAccount(String accountNo) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean chooseAndConfirmAmount(float amount) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
