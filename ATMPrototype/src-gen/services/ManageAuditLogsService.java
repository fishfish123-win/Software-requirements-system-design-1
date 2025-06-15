package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageAuditLogsService {

	/* all system operations of the use case*/
	boolean insertFIPSToken(String token) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean selectLogRange(LocalDate start, LocalDate end) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
