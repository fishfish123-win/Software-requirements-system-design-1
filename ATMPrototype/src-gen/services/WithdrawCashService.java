package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface WithdrawCashService {

	/* all system operations of the use case*/
	boolean insertDebitCard(String cardID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputPIN(String pinCode) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean selectWithdrawal(String optionID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean chooseAmount(int selectedAmount) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean takeCash() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
