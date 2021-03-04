package business;

import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

import dataaccess.ConfigurationRowDataGateway;
import dataaccess.CustomerRowDataGateway;
import dataaccess.PersistenceException;
import dataaccess.ProductRowDataGateway;
import dataaccess.RecordNotFoundException;
import dataaccess.SaleProductRowDataGateway;
import dataaccess.SaleRowDataGateway;
import datatypes.SaleStatus;
import facade.exceptions.ApplicationException;

/**
 * Handles sale's transactions. 
 * Each public method implements a transaction script.	
 *	
 * @author fmartins
 * @author alopes
 * @version 1.3 (18/03/2021)
 * 
 */
public class SaleTransactionScripts {

	/**
	 * Creates a new sale for a given customer identified by its VAT number
	 * 
	 * @param vat The VAT number of the customer the sale belongs to
	 * @return The id of the new sale. This id is needed for later being able to add products 
	 * to the sale and for computing the sale's total, the total discount and close the sale.
	 * @throws ApplicationException When the customer does not exist.
	 */ 
	public int newSale (int vat) throws ApplicationException {		
		// gets the customer number having its vat number
		Optional<CustomerRowDataGateway> customer = CustomerRowDataGateway.findByVATNumber (vat);
		if (customer.isPresent()) {
			// creates the sale
			SaleRowDataGateway newSale = new SaleRowDataGateway(customer.get().getCustomerId(), new Date());
			newSale.insert();
			return newSale.getId();
		} else {
			throw new ApplicationException("There is no customer with the given VAT number " + vat);
		}
	}


	/**
	 * Adds a product to an open sale.
	 * 
	 * @param saleId The sale's id.
	 * @param productCode The product's code.
	 * @param qty The amount to sell.
	 * @throws ApplicationException When the sale is closed, the product code does not exist, or
	 * there's not enough stock of the product. 
	 * 
	 * @requires qty > 0
	 */
	public void addProductToSale (int saleId, int productCode, double qty) throws ApplicationException {
		// Checks if a sale with saleId exists and its status is still open
		SaleRowDataGateway sale = getSale(saleId);
		if (sale.getStatus() == SaleStatus.CLOSED)
			throw new ApplicationException("Products cannot be added to closed sales.");

		// gets the product id from its code
		ProductRowDataGateway product = getProduct(productCode);

		// if it has available units in stock...
		if (product.getQty() >= qty) {
			addProductToSale(product, qty, sale);
		} else {
			throw new ApplicationException("Product with code " + productCode + " has ("  + 
					product.getQty() + ") units, which is insufficient for the request of " + 
					qty + " units.");
		}
	}

	/**
	 * Gets a SaleRowGateway object for a given sales id, or raises a 
	 * RecordNotFoundException otherwise.
	 * 
	 * @param saleId The sale id to get the SaleRowGateway object
	 * @return The SaleRowGateway object associated with the given saleId 
	 * @throws ApplicationException When there is no sale with saleId 
	 */
	private SaleRowDataGateway getSale(int saleId) throws ApplicationException {
		Optional<SaleRowDataGateway> sale = SaleRowDataGateway.find(saleId);
		if (sale.isPresent())
			return sale.get();
		else 
			throw new ApplicationException("Sale with id: " + saleId + " does not exist.");
	}

	/**
	 * Gets a ProductRowGateway object for a given productCode, or raises a 
	 * RecordNotFoundException otherwise.
	 * 
	 * @param productCode The product code to get the ProductRowGateway object
	 * @return The ProductRowGateway object associated with the give productCode
	 * @throws ApplicationException When there is no product with the given productCode
	 */
	private ProductRowDataGateway getProduct(int productCode) throws ApplicationException {
		Optional<ProductRowDataGateway> product = ProductRowDataGateway.findWithProdCod(productCode);
		if (product.isPresent())
			return product.get();
		else
			throw new ApplicationException("Product with code " + productCode + " does not exist.");
	}

	/**
	 * Performs a product sale by updating its stock availability and by associating 
	 * this information with the sale.
	 * 
	 * @param product The product being sold
	 * @param qty The number of units to be sale
	 * @param sale The sale to associate the product sale with
	 */
	private void addProductToSale(ProductRowDataGateway product, double qty, SaleRowDataGateway sale) {
		// decrement the stock
		product.setQty(product.getQty() - qty);
		product.updateStockValue();
		// adds product to sale 
		SaleProductRowDataGateway productSale = new SaleProductRowDataGateway(sale.getId(), 
				product.getProductId(), qty);
		productSale.insert();
	}


	/**
	 * Computes the discount amount for a sale (based on the discount type of the customer).
	 * 
	 * @param saleId The sale's id
	 * @return The discount amount
	 * @throws ApplicationException When the sale does not exist or when an unexpected 
	 * referential integrity problem occurs.
	 */
	public double getSaleDiscount (int saleId) throws ApplicationException {
		try {
			SaleRowDataGateway sale = getSale(saleId);
			// If the sale is closed, the discount is already computed
			if (sale.getStatus() == SaleStatus.CLOSED) 
				return sale.getDiscount();

			// Get customer associated with the sale. 
			// The customer always exists due to the referential integrity enforced by the database
			CustomerRowDataGateway customer = CustomerRowDataGateway.find(sale.getCustomerId());

			// Get sale's products
			Iterable<SaleProductRowDataGateway> saleProducts = SaleProductRowDataGateway.findSaleProducts(saleId);

			// Compute the sale discount 
			return computeDiscount(customer, saleProducts);	
		} catch (PersistenceException e) { 
			throw new ApplicationException("Internal referential integrity error when computing "
					+ "the discount for sale " + saleId, e);			
		}
	}


	/**
	 * Computes the discount for the products of a sale to a customer
	 * 
	 * @param customer The customer the sale is made to
	 * @param saleProducts The products being sold
	 * @return The discount amount based on the discount type associated to the customer
	 * @throws PersistenceException When there is an internal referential integrity problem
	 */
	private double computeDiscount(CustomerRowDataGateway customer, 
			Iterable<SaleProductRowDataGateway> saleProducts) throws PersistenceException {
		switch (customer.getDiscountType()) {
		case SALE_AMOUNT:
			return discountOnSaleAmount (saleProducts);
		case ELIGIBLE_PRODUCTS:
			return discountOnEligibleProducts (saleProducts);
		default:
			return 0;
		}
	}


	/**
	 * Computes a discount based on the sale's amount.
	 * 
	 * @param saleProducts The products on which to compute the discount
	 * @return The discount value
	 * @throws PersistenceException When there is an internal referential integrity problem
	 */
	private double discountOnSaleAmount(Iterable<SaleProductRowDataGateway> saleProducts) 
			throws PersistenceException {
		// Determines the sale's total. Mind the Java 8 lambda function (p -> true).
		// In this case it selects all products.
		double saleTotal = computeSaleTotal(saleProducts, p -> true);

		// get configuration info.
		ConfigurationRowDataGateway config = ConfigurationRowDataGateway.loadConfiguration();

		return saleTotal > config.getAmountThreshold() ? saleTotal * config.getAmountThresholdPercentage() : 0;
	}


	/**
	 * Computes a discount based on the amount of eligible products of the sale.
	 * 
	 * @param saleProducts The set with the sold products 
	 * @return The discount value
	 * @throws PersistenceException When some unexpected referential integrity problem occurs.
	 */
	private double discountOnEligibleProducts(Iterable<SaleProductRowDataGateway> saleProducts) 
			throws PersistenceException {
		// Mind the method reference. In the case computeSaleTotal filters 
		// the products that are eligible for discount.
		double saleEligibleTotal = computeSaleTotal(saleProducts, ProductRowDataGateway::isEligibleForDiscount);

		// get configuration info.
		ConfigurationRowDataGateway config = ConfigurationRowDataGateway.loadConfiguration();

		return saleEligibleTotal * config.getEligiblePercentage();
	}


	/**
	 * Computes the sale's total of eligible products, or throws a RecordNotFoundException in case 
	 * there is a referential integrity problem (the sales refers to a product that does not exits). 
	 * 
	 * Notice the use of Predicate<ProductRowDataGateway>; it "receives" a ProductRowDataGateway and
	 * will apply the predicate to it. Notice that method isEligibleForDiscount passed in
	 * method discountOnEligibleProducts is of this type: a predicate over ProductRowDataGateway
	 * elements. Then, the if statement applies the filter passed as a parameter. 
	 * 
	 * @param saleProducts The products associated with the sale
	 * @return sale's total of eligible products
	 * @throws RecordNotFoundException When there is a referential integrity problem
	 */
	private double computeSaleTotal(Iterable<SaleProductRowDataGateway> saleProducts,
			Predicate<ProductRowDataGateway> filter) throws RecordNotFoundException {
		double saleEligibleTotal = 0;		
		for (SaleProductRowDataGateway saleProduct : saleProducts) {
			// Get product info. Always exists, unless there is an referential integrity problem.
			ProductRowDataGateway product = ProductRowDataGateway.find(saleProduct.getProductId());
			if (filter.test(product))
				saleEligibleTotal += saleProduct.getQty() * product.getFaceValue();
		}
		return saleEligibleTotal;
	}

	/**
	 * Closes an open sale.
	 * 
	 * @param saleId The sale's id.
	 * @throws ApplicationException When the sale does not exists or is already closed.
	 */
	public void closeSale (int saleId) throws ApplicationException {
		//TODO: implement me
	}


}
