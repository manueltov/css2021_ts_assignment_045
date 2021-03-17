package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import datatypes.DiscountType;

/**
 * An in-memory representation of a customer table record. 
 *	
 * Notes:
 * 1. The SQL string should not be in this class because it limits 
 * the database portability. In version 2 we will move it to an appropriate
 * class.
 * 
 * 2. This class raises exceptions defined in this package, not the SQL 
 * exceptions thrown by the underlying connection with the database engine 
 * (in the Java case, JDBC). However, low-level SQL exceptions are 
 * wrapped in the thrown exception, so it can be used for debugging purposes.
 * These exceptions can be obtained by using the getCause method from the
 * Exception class.
 * <p>
 * 3. The method that inserts a new client in the database, uses method
 * getGeneratedKeys to obtain the clientId that is generated
 * automatically (auto-incremented) by the database engine.
 * <p>
 * 4. The prepared statement takes and extra parameter when we want to obtain
 * the generated keys. See DataSource.prepareGetGenKey(...).
 * <p>
 * 5. Database resources should be closed as soon as possible. Java 7 includes a
 * new try construct (try-with-resources) that closes the resources automatically at
 * the end of the try-catch block. In previous versions we need to use a finally
 * block in order to close ResultSets and Statements in each method.
 * More information about Java 7 try-with-resources constructor can be found in
 * http://download.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html.
 * <p>
 * 6. The usage of Singletons (as the case of DataSource) makes tests more difficult.
 * I programmed in this way, so it is simpler to understand. In version 2 I'll solve 
 * this problem. The solution involves the usage of factories of objects and it a bit
 * more involving. Java EE also provides dependency injection which is more elegant
 * (with less boiler plate code) but even harder to understand.
 * <p>
 * 7. I use static methods to get the information from the database and to solve the
 * problem of "who creates the objects from the customer row gateway". Another way is
 * to use a specific Finder class for creating these objects. Version 2 of the
 * project illustrates this other approach.
 *
 * @author fmartins
 * @version 1.3 (29/10/2016)
 */
public class CustomerRowDataGateway {

    // Customer attributes

    /**
     * Customer's internal identification (unique, primary key, sequential)
     * generated automatically by the database engine.
     */
    private int id;

    /**
     * Customer's VAT number
     */
    private int vat;

    /**
     * Customer's name. In case of a company, the represents its commercial denomination
     */
    private String designation;

    /**
     * Customer's contact number
     */
    private int phoneNumber;

    /**
     * Customer's discount id. This discount is applied to eligible products.
     */
    private int discountId;


    // Database schema constants

    /**
     * Table name
     */
    private static final String TABLE_NAME = "customer";

    /**
     * Field names
     */
    private static final String ID_COLUMN_NAME = "id";
	private static final String VAT_NUMBER_COLUMN_NAME = "vatnumber";
    private static final String DESIGNATION_COLUMN_NAME = "designation";
    private static final String PHONE_NUMBER_COLUMN_NAME = "phonenumber";
    private static final String DISCOUNT_ID_COLUMN_NAME = "discount_id";

	/**
	 * Constants for mapping discount types with discount ids
	 */
	private static final int NO_DISCOUNT = 1;
	private static final int SALE_AMOUNT = 2;
	private static final int ELIGIBLE_PRODUCTS = 3;

	// SQL statements
	
    /**
	 * insert a customer 
	 */
	private static final String INSERT_CUSTOMER_SQL =
			"insert into " + TABLE_NAME + " " +
               "(" + ID_COLUMN_NAME + ", " + VAT_NUMBER_COLUMN_NAME + ", " + DESIGNATION_COLUMN_NAME + ", " + 
				PHONE_NUMBER_COLUMN_NAME + ", " + DISCOUNT_ID_COLUMN_NAME + ") " +
            "values (DEFAULT, ?, ?, ?, ?)";

	/**
	 * obtain a customer by VAT
	 */
	private static final String GET_CUSTOMER_BY_VAT_NUMBER_SQL =
			"select " + ID_COLUMN_NAME + ", " + VAT_NUMBER_COLUMN_NAME + ", " + DESIGNATION_COLUMN_NAME + ", " + 
					PHONE_NUMBER_COLUMN_NAME + ", " + DISCOUNT_ID_COLUMN_NAME + " " +
            "from " + TABLE_NAME + " " +
            "where " + VAT_NUMBER_COLUMN_NAME + " = ?";

	/**
	 * obtain a customer by id
	 */
	private static final String	GET_CUSTOMER_BY_ID_SQL =
			"select " + ID_COLUMN_NAME + ", " + VAT_NUMBER_COLUMN_NAME + ", " + DESIGNATION_COLUMN_NAME + ", " + 
					PHONE_NUMBER_COLUMN_NAME + ", " + DISCOUNT_ID_COLUMN_NAME + " " +
            "from " + TABLE_NAME + " " +
            "where " + ID_COLUMN_NAME + " = ?";
	
	/**
	 * an exception logger
	 */
	private static Logger log = Logger.getLogger(CustomerRowDataGateway.class.getCanonicalName());
	
    // 1. constructor

    /**
     * Creates a new customer given its VAT number, its designation, phone contact, and discount type.
     *
     * @param vat          The customer's VAT number
     * @param designation  The customer's designation
     * @param phoneNumber  The customer's phone number
     * @param discountType The customer's discount type
     */
    public CustomerRowDataGateway(int vat, String designation, int phoneNumber,
                                  DiscountType discountType) {
        this.vat = vat;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
        setDiscountType(discountType);
    }


    // 2. getters and setters

    /**
     * @return The id of the customer
     * <p>
     * There is no need for a setter, since this is an internal
     * database number (a sequential the primary key) that is not
     * available at the application level for change.
     */
    public int getCustomerId() {
        return id;
    }

    /**
     * Comment: There is a business logic decision not to allow changes to the VAT number
     * of a customer. We can reflect this decision in the row data gateway by not providing
     * the setVAT method. However, in a more mechanical approach where every field has a
     * getter and a setter, it is perfectly plausible to provide a setter method as well.
     * <p>
     * Whenever there is repeated code (specially when using transaction script),
     * these decisions can migrate to the row gateway and it starts being converted
     * into an active record style. Nevertheless, use it with care and bare in mind that
     * business rules are enforced by the domain logic. When the "migration" to active record
     * starts, please move the class to the business logic package.
     *
     * @return The customer's VAT number.
     */
    public int getVAT() {
        return vat;
    }

    /**
     * @return The customer's designation.
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Updates the customer designation.
     *
     * @param designation The new designation to change to.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return The customer's phone number
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Updates the phone number
     *
     * @param phoneNumber The new phone number
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

	/**
	 * Notice that the setter and the getter converts the integer representing
	 * the discount id to an enumerated value (of type DiscountType) that is 
	 * then used in the application's domain logic. The internal id (primary
	 * key) must not be used directly for implementing business rules.
	 * 
	 * Observation: should the persistence layer refers to a DiscountType class that
	 * is at the application layer? On the other hand, shall the application level,
	 * in particular the DiscountType class, take care of value conversion for 
	 * serialization purposes? 
	 * 
	 * @return The discount id associated with the customer
	 */
	public DiscountType getDiscountType() {
        return toDiscountType(discountId);
    }

    /**
     * Updates the discount id of the customer
     *
     * @param discountType The new discount type
     */
    public void setDiscountType(DiscountType discountType) {
        this.discountId = discountType == DiscountType.SALE_AMOUNT ? SALE_AMOUNT :
                discountType == DiscountType.ELIGIBLE_PRODUCTS ? ELIGIBLE_PRODUCTS : NO_DISCOUNT;
    }

	/**
	 * Converts an integer into a discount type. Used for decoding the persisted value.
	 *
	 * @param discountId The integer to convert into a discount type. Invalid discount types
	 * are converted to DiscountType.NO_DISCOUNT.
	 * @return The discount type corresponding to an integer.
	 */
	private static DiscountType toDiscountType(int discountId) {
        return discountId == ELIGIBLE_PRODUCTS ? DiscountType.ELIGIBLE_PRODUCTS :
                discountId == SALE_AMOUNT ? DiscountType.SALE_AMOUNT : DiscountType.NO_DISCOUNT;
	}


	// 3. interaction with the repository (an SQL database) using JDBC

	/**
	 * Stores the information in the database.
	 * When the information is stored, the customer id gets generated by
	 * the database. I get it using the getGeneratedKeys method and update
	 * the customer's id attribute.
	 * We will always use prepared statement to interact with the database. There
	 * are several advantages of doing so, like, for instance:
	 *
	 * 1. The execution of subsequent commands is faster because the database
	 * parses the command just once and, in case of complex queries, devises the
	 * strategy to access the information just once (as well)
	 *
	 * 2. It prevents SQL injections, since the command is parsed before values are
	 * sent, and when values are sent they are treated as values, not as a string that
	 * is part of the command itself.
	 */
	public void insert () throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_CUSTOMER_SQL)) {
			// set statement arguments
			statement.setInt(1, vat);
			statement.setString(2, designation);
			statement.setInt(3, phoneNumber);
			statement.setInt(4, discountId);
			// executes SQL
			statement.executeUpdate();
			// Gets customer Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next();
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error!", e);
		}
	}

	/**
	 * Fetches a customer given its internal id number and returns a CustomerRowGateway 
	 * object with the data retrieved from the database. In case the customer
	 * is not found, a RecordNotFoundException is thrown.

	 * @param id The Id of the customer to fetch from the repository.
	 * @return The CustomerRowGateway corresponding to the customer with the id number.
	 * @throws RecordNotFoundException When the customer with the given id number is not found.
	 */
	public static CustomerRowDataGateway find (int id) throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_CUSTOMER_BY_ID_SQL)) {			
			// set statement arguments
			statement.setInt(1, id);
			// executes SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer from the data retrieved from the database
				return loadCustomer(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting a customer by its id", e);
		}
	}

	/**
	 * Fetches a customer given its VAT number and returns a CustomerRowGateway
	 * object with the data retrieved from the database. In case the customer
	 * is not found, a RecordNotFoundException is thrown. I use a static method
	 * for processing the result set and for creating the Customer Row Gateway
	 * object. This way I avoid repeating the code in the methods implementing
	 * various forms of obtaining records from the database.
	 *
	 * @param vat The VAT number of the customer to fetch from the database
	 * @return The CustomerRowGateway corresponding to the customer with the vat number.
	 * @throws RecordNotFoundException When the customer with the vat number is not found.
	 */
	public static Optional<CustomerRowDataGateway> findWithVATNumber (int vat) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_CUSTOMER_BY_VAT_NUMBER_SQL)) {
			// set statement arguments
			statement.setInt(1, vat);
			// executes SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer from the data retrieved from the database
				return Optional.of(loadCustomer(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			log.log(Level.SEVERE, "Internal error getting a customer by its VAT number", e);
			return Optional.empty();
		}
	}
	
	/**
	 * Creates a customer from a result set retrieved from the database.
	 * 
	 * @param rs The result set with the information to create the customer.
	 * @return A new customer loaded from the database.
	 * @throws RecordNotFoundException In case the result set is empty.
	 */
	private static CustomerRowDataGateway loadCustomer(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			CustomerRowDataGateway newCustomer = new CustomerRowDataGateway(rs.getInt(VAT_NUMBER_COLUMN_NAME),
					rs.getString(DESIGNATION_COLUMN_NAME), rs.getInt(PHONE_NUMBER_COLUMN_NAME),
					toDiscountType(rs.getInt(DISCOUNT_ID_COLUMN_NAME)));
			newCustomer.id = rs.getInt(ID_COLUMN_NAME);
			return newCustomer;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Customer does not exist", e);
		}
	}
}