package com.priceminister.account;

/**
 * This class represents a simple account.
 * It doesn't handle different currencies, all money is supposed to be of standard currency EUR.
 */
public interface Account {
    
    /**
     * Adds money to this account.
     * @param addedAmount - the money to add
     * @throws IllegalValueException if the value is greater than 0
     * @ThreadSafe 
     */
    public void add(Double addedAmount) throws IllegalValueException;
    
    /**
     * Withdraws money from the account.
     * @param withdrawnAmount - the money to withdraw
     * @param rule - the AccountRule that defines which balance is allowed for this account
     * @return the remaining account balance
     * @throws IllegalBalanceException if the withdrawal leaves the account with a forbidden balance
     * @ThreadSafe 
     */
    public Double withdrawAndReportBalance(Double withdrawnAmount, AccountRule rule) throws IllegalBalanceException;
    
    /**
     * Gets the current account balance.
     * @return the account's balance
     * @ThreadSafe 
     */
    public Double getBalance();
}
