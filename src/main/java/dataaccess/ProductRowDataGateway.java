package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An in-memory representation of a product table record.
 * <p>
 * Notes:
 * 1. See the notes for the CustomerRowGateway class
 *
 * @author fmartins
 * @author malopes
 * @version 1.3 (05/03/2018)
 *
 */
public class ProductRowDataGateway {

    // Product attributes

    /**
     * Product id (unique, primary key)
     */
    private int id;

    /**
     * The product code. This code is the one present in the product that customers have access to.
     * It is different from the id, which is an internal identity, used for relations with other
     * entities (e.g., Sale)
     */
    private int prodCod;

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


    // Database schema constants

    /**
     * Table name
     */
    private static final String TABLE_NAME = "product";

    /**
     * Field names
     */
    private static final String ID_COLUMN_NAME = "id";
    private static final String PROD_COD_COLUMN_NAME = "prodcod";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String FACE_VALUE_COLUMN_NAME = "facevalue";
    private static final String QTY_COLUMN_NAME = "qty";
    private static final String DISCOUNT_ELIGIBILITY_COLUMN_NAME = "discounteligibility";
    private static final String UNIT_ID_COLUMN_NAME = "unit_" + ID_COLUMN_NAME;

    /**
	 * Constants for mapping eligible discount to a string
	 */
	private static final String ELIGIBLE = "E";
	private static final String NOT_ELIGIBLE = "N";

	// SQL statements

	/**
	 * obtain a product by product code 
	 */
	private static final String GET_PRODUCT_BY_PROD_COD_SQL =
		    "select " + ID_COLUMN_NAME + ", " + PROD_COD_COLUMN_NAME + ", " + DESCRIPTION_COLUMN_NAME + ", " + 
		    		FACE_VALUE_COLUMN_NAME + ", " + QTY_COLUMN_NAME + ", " +
					DISCOUNT_ELIGIBILITY_COLUMN_NAME + ", " + UNIT_ID_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
			"where " + PROD_COD_COLUMN_NAME + " = ?";

	/**
	 * obtain a product by Id 
	 */
	private static final String GET_PRODUCT_BY_ID_SQL =
		    "select " + ID_COLUMN_NAME + ", " + PROD_COD_COLUMN_NAME + ", " + DESCRIPTION_COLUMN_NAME + ", " + 
		    		FACE_VALUE_COLUMN_NAME + ", " + QTY_COLUMN_NAME + ", " +
                    DISCOUNT_ELIGIBILITY_COLUMN_NAME + ", " + UNIT_ID_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
			"where " + ID_COLUMN_NAME + " = ?";

	/**
	 * update product stock
	 */
	private static final String	UPDATE_STOCK_SQL =
			"update " + TABLE_NAME + " " +
			"set " + QTY_COLUMN_NAME + " = ? " +
			"where " + ID_COLUMN_NAME + " = ?";

	/**
	 * an exception logger
	 */
	private static Logger log = Logger.getLogger(ProductRowDataGateway.class.getCanonicalName());

    // 1. getters and setters

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
		return discountEligibility.equals(ELIGIBLE);
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

	
	// 2. interaction with the repository (a database in this simple example)
	
	// Since this first version of the application does not add new products, 
	// there is no insert method.	

	/**
	 * Gets a product given its product id
	 *
	 * @param id The id of the product to search for
	 * @return The in-memory representation of the product
	 * @throws PersistenceException In case there is an error accessing the database.
	 */
	public static Optional<ProductRowDataGateway> find(int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_PRODUCT_BY_ID_SQL)) {
			// set statement arguments
			statement.setInt(1, id);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new product with the data retrieved from the database
				return Optional.of(loadProduct(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			log.log(Level.SEVERE, "Internal error getting product with id" + id, e);
			return Optional.empty();
		}
	}

	/**
	 * Gets a product given its codProd
	 *
	 * @param prodCod The codProd of the product to search for
	 * @return The in-memory representation of the product
	 */
	public static Optional<ProductRowDataGateway> findWithProdCod (int prodCod)  {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_PRODUCT_BY_PROD_COD_SQL)) {
			// set statement arguments
			statement.setInt(1, prodCod);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new product with the data retrieved from the database
				return Optional.of(loadProduct(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			log.log(Level.SEVERE, "Internal error getting product with id" + prodCod, e);
			return Optional.empty();
		}
	}
	
	/**
	 * Updates the product quantity
	 *
	 * @throws PersistenceException An error updating the product stock amount
	 */
	public void updateStock () throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(UPDATE_STOCK_SQL)){
			// set statement arguments
			statement.setDouble(1, qty);
			statement.setInt(2, id);
			// execute SQL
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException("Internal error updating product stock amount.", e);
		}
	}

	/**
	 * Creates a product from a result set retrieved from the database.
	 *
	 * @param rs The result set with the information to create the product.
	 * @return A new product loaded from the database.
	 * @throws RecordNotFoundException In case the result set is empty.
	 */
	private static ProductRowDataGateway loadProduct(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			ProductRowDataGateway newProduct = new ProductRowDataGateway();
			newProduct.id = rs.getInt(ID_COLUMN_NAME);
			newProduct.prodCod = rs.getInt(PROD_COD_COLUMN_NAME);
			newProduct.description = rs.getString(DESCRIPTION_COLUMN_NAME);
			newProduct.faceValue = rs.getDouble(FACE_VALUE_COLUMN_NAME);
			newProduct.qty = rs.getDouble(QTY_COLUMN_NAME);
			newProduct.discountEligibility = rs.getString(DISCOUNT_ELIGIBILITY_COLUMN_NAME);
			newProduct.unitId = rs.getInt(UNIT_ID_COLUMN_NAME);
			return newProduct;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Product not found", e);
		}
	}
}
