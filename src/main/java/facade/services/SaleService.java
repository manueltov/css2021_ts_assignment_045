package facade.services;

import business.SaleTransactionScripts;
import facade.exceptions.ApplicationException;

/**
 * A sale service. The purpose of this class is to provide access to the business
 * layer, hiding its implementation from the clients. Clients should never interact 
 * directly with the business layer, so this very simple singleton offers a method 
 * to add a new sale, add a product to the sale, obtain the sale discount and close the sale. 
 * As you will see in future, different implementation of the
 * business and data access layer will not change this interface and, consequently, 
 * the way clients interact with the application.
 * 
 * @author fmartins
 * @author alopes
 * @version 1.2 (18/03/2021)
 */
public enum SaleService {
	INSTANCE;

	/**
	 * The business object implementing the sale services
	 */
	private final SaleTransactionScripts saleTS;

	/**
	 * There is only a single object of this class. 
	 */
	private SaleService() {
		saleTS = new SaleTransactionScripts();
	}

	/**
	 * Add a new sale to a customer with a giving VAT number.
	 * 
	 * @param vat The VAT number of the customer purchasing goods. 
	 * @return The sale identification. The id must be used when adding
	 * products to the sale.
	 * @throws ApplicationException In case the sale could not be created.
	 */
	public int newSale(int vat) throws ApplicationException {
		return saleTS.newSale(vat);
	}

	/**
	 * Add a product to a given sale.
	 * 
	 * @param saleId The id of the sale to add the product to.
	 * @param productCode The code of the product to be added to the sale.
	 * @param qty The quantity being purchased. 
	 * @throws ApplicationException In case the product could not be added
	 * to the sale. See lower level exception for detailed information.
	 */
	public void addProductToSale(int saleId, int productCode, int qty) 
			throws ApplicationException {
		saleTS.addProductToSale(saleId, productCode, qty);
	}

	/**
	 * @param saleId The id of the sale the obtain the discount total
	 * @return The total discount for a sale with id saledId.
	 * @throws ApplicationException In case the sale does not exists.
	 */
	public double getSaleDiscount(int saleId) throws ApplicationException {
		return saleTS.getSaleDiscount(saleId);
	}
	
	/**
	 * @param saleId The id of the sale to close
	 * @throws ApplicationException In case the sale does not exists or is already closed.
	 */
	public void closeSale(int saleId) throws ApplicationException {
		saleTS.closeSale(saleId);
	}

}
