package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ReplenishCashService {

	/* all system operations of the use case*/
	boolean insertPhysicalKey(String keyID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputCredentials(String operatorID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean loadBanknoteBundle(String denomination, float count) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
