package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * An in-memory representation of a row gateway for the products composing a sale.
 *
 * @author fmartins
 * @version 1.1 (19/02/2015)
 */
public class SaleProductRowDataGateway {

    // Sale product attributes

    private int id;

    /**
     * The sale the product belongs to
     */
    private int saleId;

    /**
     * The product of the sale
     */
    private int productId;

	/**
	 * The number of items purchased
	 */
	private double qty;


	// Database schema constants

	/**
	 * Table name
	 */
	private static final String TABLE_NAME = "saleproduct";

	/**
	 * Field names
	 */
	private static final String ID_COLUMN_NAME = "id";
	private static final String SALE_ID_COLUMN_NAME = "sale_id";
	private static final String PRODUCT_ID_COLUMN_NAME = "product_id";
	private static final String QTY_COLUMN_NAME = "qty";

	// SQL statements

	/**
	 * insert a product in a sale
	 */
	private static final String INSERT_PRODUCT_SALE_SQL = 
			"insert into " + TABLE_NAME + 
				" (" + ID_COLUMN_NAME + ", " + SALE_ID_COLUMN_NAME + ", " + PRODUCT_ID_COLUMN_NAME + ", " + 
					QTY_COLUMN_NAME + ") " +
			"values (DEFAULT, ?, ?, ?)";

	/**
	 * obtain the products of a sale by sale Id 
	 */
	private static final String GET_SALE_PRODUCTS_SQL =
			"select " + ID_COLUMN_NAME + ", " + SALE_ID_COLUMN_NAME + ", " + PRODUCT_ID_COLUMN_NAME + ", " + 
					QTY_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
			"where " + SALE_ID_COLUMN_NAME + " = ?";


	// 1. constructor

    /**
     * Creates an association between a saleId and a productId. The qty is the
     * quantity of items in the sale.
     *
     * @param saleId    The sale to associate a product to.
     * @param productId The product to be associated with the sale.
     * @param qty       The number of products sold.
     */
    public SaleProductRowDataGateway(int saleId, int productId, double qty) {
        this.saleId = saleId;
        this.productId = productId;
        this.qty = qty;
    }


    // 2. getters and setters

    /**
     * @return The product sale id
     */
    public int getId() {
        return id;
    }

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


    // 3. interaction with the repository (a relational database in this simple example)

	/**
	 * Inserts the record in the products sale
	 */
	public void insert () throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_PRODUCT_SALE_SQL)) {
			// set statement arguments
			statement.setInt(1, saleId);
			statement.setInt(2, productId);
			statement.setDouble(3, qty);
			// execute SQL
			statement.executeUpdate();
			// Gets sale product Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next();
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new product in a sale!", e);
		}
	}

	/**
	 * Gets the products of a sale by its sale id
	 *
	 * @param saleId The sale id to get the products of
	 * @return The set of products that compose the sale
	 * @throws PersistenceException When there is an error obtaining the
	 *         information from the database.
	 */
	public static Iterable<SaleProductRowDataGateway> findSaleProducts (int saleId) throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_SALE_PRODUCTS_SQL)) {
			// set statement arguments
			statement.setInt(1, saleId);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates the sale's products set with the data retrieved from the database
				return loadSaleProducts(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting the products of a sale", e);
		}
	}

	/**
	 * Creates the set of products of a sale from a result set retrieved from the database.
	 *
	 * @param rs The result set with the information to create the set of products from a sale.
	 * @return The set of products of a sale loaded from the database.
	 * @throws SQLException When there is an error reading from the database.
	 */
	private static Set<SaleProductRowDataGateway> loadSaleProducts(ResultSet rs) throws SQLException {
		Set<SaleProductRowDataGateway> result = new HashSet<>();
		while (rs.next()) {
			SaleProductRowDataGateway newSaleProduct = new SaleProductRowDataGateway(rs.getInt(SALE_ID_COLUMN_NAME),
					rs.getInt(PRODUCT_ID_COLUMN_NAME), rs.getDouble(QTY_COLUMN_NAME));
			newSaleProduct.id = rs.getInt(ID_COLUMN_NAME);
			result.add(newSaleProduct);
		}
		return result;		
	}
}
