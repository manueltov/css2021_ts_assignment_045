package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import datatypes.SaleStatus;

/**
 * An in-memory representation of a customer table record.
 * <p>
 * Notes:
 * 1. See the notes for the CustomerRowGateway class
 * <p>
 * 2. Java makes available two Date classes (in fact, more in Java 8,
 * but we will address it later (with JPA)): one in the package
 * java.util, which is the one we normally use, and another in
 * java.sql, which is a subclass of java.util.date and that
 * transforms the milliseconds representation according to the
 * "Date type of databases". For more information refer to
 * http://download.oracle.com/javase/6/docs/api/java/sql/Date.html.
 * <p>
 * 3. When creating a new sale, we only pass the date and customer id
 * parameters to the constructor. Moreover, attribute open is always
 * set to 'O'. The remaining attributes are either set automatically (id)
 * or when closing the sale (totalSale and totalDiscount). Also, the open
 * attribute is set to 'C' upon payment.
 *
 * @author fmartins
 * @author malopes
 * @version 1.3 (05/03/2018)
 *
 */
public class SaleRowDataGateway {

    // Sale attributes

    /**
     * The sale's id (unique, primary key)
     */
    private int id;

    /**
     * The date the sale was made
     */
    private java.sql.Date date;

	/**
	 * The sale's total
	 */
	private double total;
	
	/**
	 * The total discount for the sale
	 */
	private double discount;
	
	/**
	 * Whether the sale is open or closed. See constants below.
	 */
	private String status;
	
	/**
	 * The customer Id the sales refers to
	 */
	private int customerId;


    // Database schema constants

    /**
     * Table name
     */
    private static final String TABLE_NAME = "sale";

    /**
	 * Column labels
     */
    private static final String ID_COLUMN_NAME = "id";
    private static final String DATE_COLUMN_NAME = "date";
    private static final String TOTAL_COLUMN_NAME = "total";
    private static final String DISCOUNT_TOTAL_COLUMN_NAME = "discount_total";
    private static final String STATUS_COLUMN_NAME = "status";
    private static final String CUSTOMER_ID_COLUMN_NAME = "customer_id";

    /**
	 * A pair of constants to map the status of a sale to a string
	 */
	private static final String CLOSED = "C";
	private static final String OPEN = "O";
	
	
	// SQL statements

	/**
	 * insert a sale
	 */
	private static final String INSERT_SALE_SQL =
			"insert into " + TABLE_NAME + " (" + DATE_COLUMN_NAME + ", " + TOTAL_COLUMN_NAME + ", " + 
					DISCOUNT_TOTAL_COLUMN_NAME + ", " + STATUS_COLUMN_NAME + ", " + CUSTOMER_ID_COLUMN_NAME + ") " +
			"values (?, ?, ?, ?, ?)";

	/**
	 * obtain a sale by Id
	 */
	private static final String GET_SALE_SQL = 
			"select " + ID_COLUMN_NAME + ", " + DATE_COLUMN_NAME + ", " + TOTAL_COLUMN_NAME + ", " + 
					DISCOUNT_TOTAL_COLUMN_NAME + ", " + STATUS_COLUMN_NAME + ", " + CUSTOMER_ID_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
            "where " + ID_COLUMN_NAME + " = ?";

	/**
	 * an exception logger
	 */
	private static Logger log = Logger.getLogger(CustomerRowDataGateway.class.getCanonicalName());

	// 1. constructor 

    /**
     * Creates a new sale given the customer Id and the date it occurs.
     *
     * @param customerId The customer Id the sale belongs to
     * @param saleTotal The sale total
     * @param discountTotal The discount total
     * @param date The date the sale took place
     */
    public SaleRowDataGateway(int customerId, double saleTotal, double discountTotal, 
    		Date date, SaleStatus saleStatus) {
        this.date = new java.sql.Date(date.getTime());
        this.total = saleTotal;
        this.discount = discountTotal;
        setStatus(saleStatus);
        this.customerId = customerId;
    }


    // 2. getters and setters

    /**
     * @return The sale's date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return The sale's total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Updates the sales total
     *
     * @param total The new total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return The sale's discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Updates the sales total discount
     *
     * @param discount The new discount total amount
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return The sale's status.
     */
    public SaleStatus getStatus() {
        return status.equals(OPEN) ? SaleStatus.OPEN : SaleStatus.CLOSED;
    }

    /**
     * Updates the sale's status
     * Notice that the setter and the getter convert the string to an
     * enumerated value (of type SaleStatus) that is then used in the
     * application's domain logic.
     *
     * @param status The new sale status
     */
    public void setStatus(SaleStatus status) {
        this.status = status == SaleStatus.OPEN ? OPEN : CLOSED;
    }

    /**
     * @return The customer's Id associated with this sale
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Note: there is no setId because the id is automatically set
     * by the database engine.
     *
     * @return The sale's Id.
     */
    public int getId() {
        return id;
    }

	
	// 3. interaction with the repository (a memory map in this simple example)

	/**
	 * Stores the information in the repository
	 */
	public void insert () throws PersistenceException {		
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_SALE_SQL)) {
			// set statement arguments
			statement.setDate(1, date);
			statement.setDouble(2, total);
			statement.setDouble(3, discount);
			statement.setString(4, status);
			statement.setInt(5, customerId);
			// execute SQL
			statement.executeUpdate();
			// Gets sale Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next(); 
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new sale!", e);
		}
	}
	
		
	/**
	 * Gets a sale by its id
	 *
	 * @param id The sale id to search for
	 * @return The new object that represents an in-memory sale
	 * @throws PersistenceException In case there is an error accessing the database.
	 */
	public static Optional<SaleRowDataGateway> find (int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_SALE_SQL)) {
			// set statement arguments
			statement.setInt(1, id);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer with the data retrieved from the database
				return Optional.of(loadSale(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			log.log(Level.SEVERE, "Internal error getting a sale", e);
			return Optional.empty();
		}
	}

	/**
	 * Creates a sale from a result set retrieved from the database.
	 *
	 * @param rs The result set with the information to create the sale.
	 * @return A new sale loaded from the database.
	 * @throws RecordNotFoundException In case the result set is empty.
	 */
	private static SaleRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			SaleRowDataGateway newSale = new SaleRowDataGateway(rs.getInt(CUSTOMER_ID_COLUMN_NAME),
					0, 0, rs.getDate(DATE_COLUMN_NAME), null);
			newSale.id = rs.getInt(ID_COLUMN_NAME);
			newSale.total = rs.getDouble(TOTAL_COLUMN_NAME);
			newSale.discount = rs.getDouble("discount_" + TOTAL_COLUMN_NAME);
			newSale.status = rs.getString(STATUS_COLUMN_NAME);
			return newSale;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Sale does not exist	", e);
		}
	}
}
