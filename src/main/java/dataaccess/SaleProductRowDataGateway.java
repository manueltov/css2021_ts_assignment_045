package dataaccess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An in-memory representation of a row gateway for the products composing a sale.	
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public class SaleProductRowDataGateway {
	
	/**
	 * The products of a sale repository. It is a map from sales to the list of products in the sale
	 */
	private static Map<Integer, LinkedList<SaleProductRowDataGateway>> saleProducts = new HashMap<> ();  


	// Sale attributes 

	/**
	 * The sale the product belongs to
	 */
	private final int saleId;
	
	/**
	 * The product of the sale
	 */
	private final int productId;
	
	/**
	 * The number of items purchased
	 */
	private final double qty;
	
	
	// 1. constructor 

	/**
	 * Creates an association between a saleId and a productId. The qty is the 
	 * quantity of items in the sale. 
	 * 
	 * @param saleId The sale to associate a product to.
	 * @param productId The product to be associated with the sale.
	 * @param qty The number of products sold.
	 */
	public SaleProductRowDataGateway(int saleId, int productId, double qty) {
		this.saleId = saleId;
		this.productId = productId;
		this.qty = qty;
	}


	// 2. getters and setters

	/**
	 * @return The sale id of the product sale
	 */
	public int getSaleId() {
		return saleId;
	}

	/**
	 * @return The product id of the product sale
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @return The quantity of the product sale
	 */
	public double getQty() {
		return qty;
	}

	
	// 3. interaction with the repository (a memory map in this simple example)
	
	/**
	 * Inserts the record in the products sale 
	 */
	public void insert () {
		// If it is the first product of the sale, we need to create the list to 
		// hold the sale's products.
		if (!saleProducts.containsKey(saleId))
			saleProducts.put(saleId, new LinkedList<>());
		saleProducts.get(saleId).add(this);
	}
	
	/**
	 * Obtains the products of a given saleId. Raises RecordNotFoundException in 
	 * case the sale is not found.
	 * 
	 * @param saleId The sale id to obtain the products from.
	 * @return An iterable with the products of the saleId.
	 * @throws RecordNotFoundException When the sale with saleId number is not found.
	 */
	public static Iterable<SaleProductRowDataGateway> findSaleProducts (int saleId) throws RecordNotFoundException {
		List<SaleProductRowDataGateway> result = saleProducts.get(saleId);
		if (result == null)
			throw new RecordNotFoundException ("Sale with id " + saleId + " does not exist!");
		else
			return result;
	}
}
