package business;

import dataaccess.CustomerRowDataGateway;
import dataaccess.PersistenceException;
import datatypes.DiscountType;
import facade.exceptions.ApplicationException;

/**
 * Handles customer transactions.
 * Each public method implements a transaction script.
 * 
 * Notice this class is no longer a singleton as before.
 *
 * @author fmartins
 * @version 1.3 (1/04/2016)
 */
public class CustomerTransactionScripts {

    /**
     * Adds a new customer. It checks that there is no other customer in the system
     * with the same VAT.
     *
     * @param vat          The VAT number of the customer
     * @param denomination The customer's name
     * @param phoneNumber  The customer's phone
     * @param discountCode The type of discount applicable for the customer
     * @throws ApplicationException When the VAT number is already in the database, when the VAT number is
     * invalid (we check according to the portuguese legislation), or when there is some unexpected SQL error.
     */
    public void addCustomer(int vat, String denomination, int phoneNumber, int discountCode)
            throws ApplicationException {
        // Checks if it is a valid VAT number
        if (!isValidVAT(vat))
            throw new ApplicationException("Invalid VAT number: " + vat);

		// Checks if the discount code is valid. 
		if (discountCode <= 0 || discountCode > DiscountType.values().length)
			throw new ApplicationException ("Invalid Discount Code: " + discountCode);

        // Checks that denomination and phoneNumber and filled in
        if (!isFilled(denomination) || phoneNumber == 0)
            throw new ApplicationException(
                    "Both denomination and phoneNumber must be filled");

		/*
        Tries to store the customer in the database. There is a
		unique key constraint associated with the VAT number, so if the
		VAT number already exists in the database a persistence exception will
		be thrown. In this simple application I'm not distinguishing an
		internal SQL error from the duplicate key error, but it could be done
		because the JDBC throws different exception. For now, we keep it simple.
		This is the proper way, in this case, to handle unique values in a concurrent
		environment, called the optimistic approach. Since I'm not expecting many
		insertions of customers with the same VAT number, I simple assume that the number
		is not in the database. In situations where a high number of collisions is expected,
		the best way is to follow a pessimistic approach, which relies in locking
		the table, checking insertion success, and unlocking the table at the end of the
		operation. This topic will be discussed in greater detail in the context of object
		relational mapping (ORM).
		Anyway, be careful that testing for the existence and then inserting
		(without locking or any other suitable method for handling concurrency) introduces a
		race condition in the application.
		*/
        try {
            CustomerRowDataGateway newCustomer = new CustomerRowDataGateway(vat, denomination, 
            		phoneNumber, DiscountType.values()[discountCode-1]);
            newCustomer.insert();
        } catch (PersistenceException e) {
            // Another important thing to notice is exception wrapping. Notice that the persistence
            // exception thrown by the underlying JDBC API is not discarded; instead, it is wrapped
            // under an application exception. This way we still know the precise problem that occurred
            // but prevent from showing low-level exceptions to the end users. It is not pleasant at
            // all for a user to see an SQL exception in her browser window.
            throw new ApplicationException("Error inserting the customer into the database", e);
        }
    }


	/**
	 * Checks if a VAT number is valid.
	 * 
	 * @param vat The number to be checked.
	 * @return Whether the VAT number is valid. 
	 */
	private boolean isValidVAT(int vat) {
		// If the number of digits is not 9, error!
		if (vat < 100000000 || vat > 999999999)
			return false;
		
		// If the first number is not 1, 2, 3, 5, 6 error!
		int firstDigit = vat / 100000000;
		if (firstDigit > 3 && firstDigit < 8 &&
			firstDigit != 5 && firstDigit != 6)
			return false;

		return vat % 10 == mod11(vat);
	}


	/**
	 * @param num The number to compute the modulus 11.
	 * @return The modulus 11 of num.
	 */
	private int mod11(int num) {
		// Checks the congruence modules 11.
		int sum = 0;
		int quocient = num / 10;
		
		for (int i = 2; i < 10 && quocient != 0; i++) {
			sum += quocient % 10 * i;
			quocient /= 10;
		}
		
		int checkDigitCalc = 11 - sum % 11;
		return checkDigitCalc == 10 ? 0 : checkDigitCalc;
	}

    /**
     * Checks is a string is filled
     *
     * @param value The String to check
     * @return true if the string is not empty (and not null!)
     */
    private boolean isFilled(String value) {
        return value != null && !value.isEmpty();
    }
}
