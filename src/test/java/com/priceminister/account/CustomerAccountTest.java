package com.priceminister.account;


import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import com.priceminister.account.Account;
import com.priceminister.account.implementation.*;


/**
 * Please create the business code, starting from the unit tests below.
 * Implement the first test, the develop the code that makes it pass.
 * Then focus on the second test, and so on.
 * 
 * We want to see how you "think code", and how you organize and structure a simple application.
 * 
 * When you are done, please zip the whole project (incl. source-code) and send it to recrutement-dev@priceminister.com
 * 
 */
public class CustomerAccountTest {
    
	private Account customerAccount;
    
    public static final Double VALUE_ZERO = 0.0; 
    public static final Double VALUE_TEN = 10.0; 
    public static final Double VALUE_TWENTY = 20.0; 
    public static final Double VALUE_TWENTY_POINT_FIVE = 20.5; 
    public static final Double VALUE_FIFTY = 50.0; 
    public static final Double VALUE_ONE_HUNDRED_AND_TWENTY = 120.0; 
    
    public static final Double VALUE_NEGATIVE_TEN =  -10.0; 
    
    public static final int NUMBER_THREADS = 3;
    public static final int TIMEOUT_TEST = 5;
    
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.customerAccount = new CustomerAccount();
    }
    
    /**
     * Tests that an empty account always has a balance of 0.0, not a NULL.
     */
    @Test
    public void testAccountWithoutMoneyHasZeroBalance() {
    	assertEquals( VALUE_ZERO , this.customerAccount.getBalance());
    }
    
    /**
     * Adds money to the account and checks that the new balance is as expected.
     */
    @Test
    public void testAddPositiveAmount() {
    	try
		{
			this.customerAccount.add(VALUE_TEN);
			assertEquals( VALUE_TEN , this.customerAccount.getBalance());
		} 
    	catch (IllegalValueException e)
		{
			fail(e.toString());
		}
    }
    
    /**
     * Adds money to the account and checks that the new balance is as expected.
     */
    @Test
    public void testAddPositivePointAmount() {
    	try
		{
			this.customerAccount.add(VALUE_TWENTY_POINT_FIVE);
			assertEquals( VALUE_TWENTY_POINT_FIVE , this.customerAccount.getBalance());
		} 
    	catch (IllegalValueException e)
		{
			fail(e.toString());
		}
    }
    
    
    /**
     * Tests that it is impossible to add a negative value
     */
    @Test
    public void testAddNegativeAmount() {
    	boolean result = false;
    	try
		{
			this.customerAccount.add(VALUE_NEGATIVE_TEN);
		} 
    	catch (IllegalValueException e)
		{
    		result = true;
		}
    	assertTrue(result);
    }
    
    /**
     * Tests that an illegal withdrawal throws the expected exception.
     * Use the logic contained in CustomerAccountRule; feel free to refactor the existing code.
     */
    @Test
    public void testWithdrawAndReportBalanceIllegalBalance()  {
    	boolean result = false;
    	try
		{
			this.customerAccount.add(VALUE_TEN);
		} 
    	catch (IllegalValueException e1)
		{
    		fail(e1.toString());
		}

    	try
		{
			this.customerAccount.withdrawAndReportBalance( VALUE_FIFTY, new CustomerAccountRule());
		} 
    	catch (IllegalBalanceException e)
		{
    		result = true;
		}
    	assertTrue(result);
    }
    
    /**
     * Tests that the return value by the withdrawal is correct
     */
    @Test
    public void testWithdrawAndReportBalance()
    {
    	try
		{
			this.customerAccount.add(VALUE_TWENTY);
		} 
    	catch (IllegalValueException e1)
		{
			fail(e1.toString());
		}
    	try
		{
			assertEquals( VALUE_TEN, this.customerAccount.withdrawAndReportBalance( VALUE_TEN, new CustomerAccountRule()));
		} 
    		catch (IllegalBalanceException e)
		{
    		fail("The value after the withdrawal must be : " + VALUE_TEN );
		}
    }
    
    /**
     * Tests the concurrent accesses for the withdrawal
     */
    @Test
    public void testConcurencyAccessForTheWithdrawal()
    {
    	ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    	
    	// 
    	Runnable testClientAccount = new Runnable()
    			{
					public void run()
					{
						try
						{
							customerAccount.add(VALUE_FIFTY);
						} 
						catch (IllegalValueException e1)
						{
							fail(e1.toString());
						}
						try
						{
							customerAccount.withdrawAndReportBalance( VALUE_TEN, new CustomerAccountRule());
						} 
						catch (IllegalBalanceException e)
						{
							fail("The value after the withdrawal must be higher to 0");
						}
					}
 
    			};
    	
    	for(int i= 0; i < NUMBER_THREADS ; i++ )
    	{
    		executorService.execute(testClientAccount);
    	}
    	
    	executorService.shutdown();
    	
    	try
		{
    		if (!executorService.awaitTermination(TIMEOUT_TEST, TimeUnit.SECONDS)) 
    		{
    			executorService.shutdownNow();
    		}
		} 
    	catch (InterruptedException e)
		{
    		// Nothing
		}	
    	
    	assertEquals( VALUE_ONE_HUNDRED_AND_TWENTY, this.customerAccount.getBalance());
    	
    }
    
}
