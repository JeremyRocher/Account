package com.priceminister.account.implementation;

import com.priceminister.account.*;


public class CustomerAccount implements Account {

	private Double amount;
	private final Object lockAccount = new Object();
	private static final Double VALUE_ZERO = 0.0;
	
	public CustomerAccount()
	{
		this.amount = 0.0;
	}
	
    public void add(Double addedAmount) throws IllegalValueException {
    	// check if the value is correct
    	if(addedAmount < VALUE_ZERO )
    	{
    		throw new IllegalValueException();
    	}
    	synchronized (lockAccount)
    	{
    		this.amount = this.amount + addedAmount;
    	}
    	AccountLogger.LOGGER.debug("Account add : "+  addedAmount);
    }

    public Double getBalance() {
    	synchronized (lockAccount)
    	{
    		return this.amount;
    	}
    }

    public Double withdrawAndReportBalance(Double withdrawnAmount, AccountRule rule) 
    		throws IllegalBalanceException {
    	AccountLogger.LOGGER.debug("The money to withdraw : "+  withdrawnAmount + " with the rule : " + rule.toString()); 
		synchronized (lockAccount)
    	{
	    	Double resultingAccountBalance = this.amount - withdrawnAmount;
	    	if(rule.withdrawPermitted(resultingAccountBalance))
	    	{
	    			this.amount = resultingAccountBalance;
	    			return resultingAccountBalance;
	        }
	    	else
	    	{
	    		throw new IllegalBalanceException(resultingAccountBalance);
	    	}
    	}
    }

}
