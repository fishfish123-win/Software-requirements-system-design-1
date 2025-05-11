// ==================== ATM System Requirements ====================

As a Customer, I want to withdraw cash, so that obtain physical currency
{
  Basic Flow {
    (User) 1. the Customer shall insert debit card into reader.
    (System) 2. When valid card detected the System shall display PIN entry interface.
    (User) 3. the Customer shall enter six digit PIN via keypad.
    (System) 4. the System shall initiate encrypted verification with bank core system.
    (System) 5. When PIN authenticated the System shall display main menu with withdrawal option.
    (User) 6. the Customer shall select Cash Withdrawal.
    (System) 7. the System shall display amount options with custom input.
    (User) 8. the Customer shall select one thousand CNY option.
    (System) 9. the System shall check account balance in real time.
    (System) 10. When balance sufficient the System shall activate cash dispensing mechanism.
    (System) 11. the System shall transport banknotes to output slot via conveyor.
    (User) 12. the Customer shall collect cash from dispenser.
    (System) 13. the System shall update account balance and generate transaction record.
    (System) 14. the System shall print receipt with timestamp amount balance.
    (User) 15. the Customer shall retrieve card and complete transaction.
  }  

  Alternative Flow {
    A. At any time, insufficient account balance :
    1. highlights Insufficient Balance warning popup.
    2. completes balance check within two hundred milliseconds.

    B. At any time, cash reserve below threshold :
    1. disables large amount options.
    2. triggers low inventory alert within one second.
  }
}

As a Customer, I want to check balance, so that monitor financial status
{
  Basic Flow {
    (User) 1. the Customer shall select Balance Inquiry from main menu.
    (System) 2. the System shall request real time balance from core banking system.
    (System) 3. the System shall decrypt and display available frozen credit balances.
    (System) 4. the System shall generate timestamped query records.
    (User) 5. the Customer shall return to main menu.
  }  

  Alternative Flow {
    A. At any time, network connection lost :
    	1.  displays cached balance.
    	2.  shows offline indicator.
  }
}

As a Maintenance Personnel, I want to replenish cash, so that maintain ATM operation
{
  Basic Flow {
    (User) 1. the Maintenance shall unlock rear panel with physical key.
    (User) 2. the Maintenance shall enter maintenance credentials.
    (System) 3. When dual authentication passed the System shall open cash safe.
    (User) 4. the Maintenance shall load new banknote bundles.
    (System) 5. the System shall initiate automatic note validation.
    (System) 6. the System shall update inventory counter with valid notes.
    (System) 7. the System shall generate cash replenishment report PDF.
    (User) 8. the Maintenance shall secure safe and exit maintenance mode.
  }  

  Alternative Flow {
    A. At any time, damaged note detected :  
    	1.  quarantines suspicious note.
    	2.  triggers audible alarm within five hundred milliseconds.
  }
}

As a System Administrator, I want to manage audit logs, so that conduct security review
{
  Basic Flow {
    (User) 1. the Admin shall insert FIPS compliant USB token.
    (System) 2. the System shall validate digital certificate.
    (User) 3. the Admin shall select log range.
    (System) 4. the System shall encrypt logs using AES standard.
    (System) 5. the System shall generate secure checksum.
    (User) 6. the Admin shall archive logs to external storage.
  }  

  Alternative Flow {
    A. At any time, storage capacity threshold reached :
    	1.  auto purges oldest logs.
    	2.  completes purge within two seconds.
  }
}

As a Customer, I want to change PIN, so that secure account access
As a Customer, I want to transfer funds, so that send money to others
As a Maintenance Personnel, I want to diagaingnose hardware, so that ensure device health
As a Maintenance Personnel, I want to update firmware, so that patch vulnerabilities
As a System Administrator, I want to configure network, so that maintain connectivity
As a System Administrator, I want to set limits, so that control financial risks