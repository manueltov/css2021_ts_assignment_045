package facade.services;

import business.CustomerTransactionScripts;
import facade.exceptions.ApplicationException;

/**
 * A customer service. The purpose of this class is to provide access to the business
 * layer, hiding its implementation from the clients. Clients should never interact 
 * directly with the business layer. One difference from v0 is that the CustomerService is
 * no longer a singleton. The use of singletons should be avoided as much as possible,
 * since it introduces a "hard" dependence between clients classes and the name of the
 * Singleton class. This, for instance, makes testing more difficult, but we will not
 * expand on it in this course.
 *  
 * As you will see in future, different implementation of the
 * business and data access layer will not change this interface and, consequently, 
 * the way clients interact with the application.
 * 
 * @author fmartins
 * @version 1.1 (16/2/2017)
 */
public class CustomerService {

	/**
	 * The business object implementing the customer services
	 */
    private CustomerTransactionScripts customerTS;

    /**
     * Constructs a customer service given the customer transaction scripts.
     * 
     * @param customerTS The customer transaction scripts
     */
    public CustomerService(CustomerTransactionScripts customerTS) {
        this.customerTS = customerTS;
    }

	/**
	 * Adds a customer given its VAT number, denomination, phone number,
	 * and discount type.
	 * 
	 * @param vat The VAT number of the customer to add to the system
	 * @param denomination The customer's denomination 
	 * @param phoneNumber The customer's phone number
	 * @param discountCode The customer's discount code
	 * @throws ApplicationException In case the customer could not be added.
	 */
    public void addCustomer(int vat, String denomination, int phoneNumber,
    		int discountCode) throws ApplicationException {
        customerTS.addCustomer(vat, denomination, phoneNumber, discountCode);
    }
}