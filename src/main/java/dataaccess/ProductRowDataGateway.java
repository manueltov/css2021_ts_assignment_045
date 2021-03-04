package dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An in-memory representation of a row gateway for a product.
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public class ProductRowDataGateway {

	/**
	 * The products repository.
	 * There are two maps because we need to access products both by product code and by product id
	 */
	private static Map<Integer, ProductRowDataGateway> productsByCode = new HashMap<> ();  
	private static Map<Integer, ProductRowDataGateway> productsById = new HashMap<> ();
	
	/**
	 * The next product id
	 */
	private static int nextId = 1; 

	// Loads two products in the repository for testing purposes.
	// It simulates a table with stored data prior to the execution of the program.
	// Notice that a static block executes just once (when the class is initialized) and that 
	// static initializers run in textual order. So, in this class, productByCode
	// is created first; then productsById, followed by nextId; after, the next
	// block of code runs. Should this block appears before the above initializations
	// and an error is going to be thrown at runtime. 
	static {
		ProductRowDataGateway product = new ProductRowDataGateway(123, "Prod 1", 100, 500, false, 1);
		productsByCode.put(123, product);
		productsById.put(1, product);
		product = new ProductRowDataGateway(124, "Prod 2", 35, 1000, true, 1);
		productsByCode.put(124, product);
		productsById.put(2, product);
	}

	
	// Product attributes 

	/**
	 * Product's internal identification (unique, primary key)
	 */
	private final int id;
	
	/**
	 * The product code. This code is the one present in the product that customers have access to.
	 * It is different from the id, which is an internal identity, used for relations with other 
	 * entities (e.g., Sale)
	 */
	private final int prodCod;
	
	/**
	 * Product's description 
	 */
	private String description;
	
	/**
	 * Product's face value
	 */
	private double faceValue;
	
	/**
	 * Product's stock quantity
	 */
	private double qty;
	
	/**
	 * If the product is eligible for a discount
	 */
	private String discountEligibility;
	
	/**
	 * Product's units
	 */
	private int unitId;

	/**
	 * Constants for mapping eligible discount to a string
	 */
	private static final String ELIGIBLE = "E";
	private static final String NOT_ELIGIBLE = "N";

	
	// 1. constructor 
	
	/**
	 * Creates a in-memory representation of a product given its
	 * product code, description, face value, quantity in stock,
	 * discount eligibility, and the units in which the quantity is
	 * expressed.
	 * 
	 * @param prodCod The product's code
	 * @param description The description of the product
	 * @param faceValue The value the product is sold
	 * @param qty The number of units available in stock
	 * @param eligibleForDiscount If the product is eligible for discount
	 * @param unitId The units in which the quantity of the product is expressed in
	 */
	public ProductRowDataGateway(int prodCod, String description,
			double faceValue, double qty, boolean eligibleForDiscount,
			int unitId) {
		id = nextId++;
		this.prodCod = prodCod;
		this.description = description;
		this.faceValue = faceValue;
		this.qty = qty;
                // FIXME: (?) overridable method call in constructor.
		setEligibleForDiscount(eligibleForDiscount);
		this.unitId = unitId;
	}

	
	// 2. getters and setters

	/**
	 * Comment: the product id is an internal (persistence) concept only used
	 * to link entities and must not have any business meaning. Also, there
	 * is no setProductId, since the id does not change through the execution
	 * of the program.
	 * 
	 * @return The product's id
	 */
	public int getProductId() {
		return id;
	}
	
	/**
	 * Comment: there is a business rule to not allow product code changes.
	 * That is why there is no method for updating the product code.
	 * 
	 * @return The code of the product.
	 */
	public int getProdCod() {
		return prodCod;
	}

	/**
	 * @return The product's description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Updates the product's description.
	 * 
	 * @param description The new description for the product 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The product's face value
	 */
	public double getFaceValue() {
		return faceValue;
	}

	/**
	 * Updates the product's face value
	 * 
	 * @param faceValue The new face value for the product
	 */
	public void setFaceValue(double faceValue) {
		this.faceValue = faceValue;
	}

	/**
	 * @return The product's quantity
	 */
	public double getQty() {
		return qty;
	}

	/**
	 * Updates the product's stock quantity
	 * 
	 * @param qty The new stock quantity
	 */
	public void setQty(double qty) {
		this.qty = qty;
	}


	/**
	 * Notice that the setter and the getter convert the string to a 
	 * boolean value that is then used in the application's domain logic.
	 * 
	 * @return whether the product is eligible for discount
	 */
	public boolean isEligibleForDiscount() {
		return discountEligibility == ELIGIBLE;
	}

	/**
	 * Updates the eligibility condition of a product.
	 * 
	 * @param eligibleForDiscount The new eligibility condition for the product.
	 */
	public void setEligibleForDiscount(boolean eligibleForDiscount) {
		this.discountEligibility = eligibleForDiscount ? ELIGIBLE : NOT_ELIGIBLE;
	}

	/**
	 * @return The units id of the product
	 */
	public int getUnitId() {
		return unitId;
	}

	/**
	 * Updates the units id of the product.
	 * 
	 * @param unitId The new units id of the product.
	 */
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	
	
	// 3. interaction with the repository (a memory map in this simple example)

	/**
	 * Finds a product with prodCod and returns its ProductRowGateway, in case
	 * it exists (Optional).
	 * 
	 * @param prodCod The code of the product to find
	 * @return The ProductRowGateway of the product with a given prodCod, in case it exists;
	 * otherwise it returns an empty optional value.
	 */
	public static Optional<ProductRowDataGateway> findWithProdCod (int prodCod) {
		return Optional.ofNullable(productsByCode.get(prodCod));
	}

	/**
	 * Finds a product given its id and returns its ProductRowGateway.
	 * Raises a RecordNotFoundException in case the id is no found.
	 * See comment in CustomerRowDataGateway.getCustomerById method.
	 *
	 * @param prodId The id of the product to find
	 * @return The ProductRowGateway of the product with a given id
	 * @throws RecordNotFoundException When the product with the given is id not found. 
	 */
	public static ProductRowDataGateway find(int prodId) throws RecordNotFoundException {
		ProductRowDataGateway result = productsById.get(prodId);
		if (result == null)
			throw new RecordNotFoundException ("Produc with id " + prodId + " does not exist	!");
		else
			return result;
	}

	/**
	 * Updates the stock value
	 */
	public void updateStockValue () {
		// Empty, since we are storing data in memory.
	}
}
