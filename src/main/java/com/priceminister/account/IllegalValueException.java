/**
 * 
 */
package com.priceminister.account;

/**
 * @author jeremy
 *
 */
public class IllegalValueException extends Exception 
{
    private static final long serialVersionUID = 125555L;
    	
    public String toString() 
    {
        return "Illegal value ! the value must be greater than 0 ";
    }
}
