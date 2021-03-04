package dataaccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import datatypes.SaleStatus;

/**
 * An in-memory representation of a row gateway for a sale row.
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public class SaleRowDataGateway {
		
	/**
	 * The sales repository
	 */
	private static Map<Integer, SaleRowDataGateway> sales = new HashMap<> (); 

	/**
	 * The next sale id
	 */
	private static int nextId = 1;
	

	// Sale attributes 

	/**
	 * The sale's id (unique, primary key)
	 * Note: the sale id is not an internal surrogate key. It is meant to be revealed 
	 * to client applications.
	 */
	private final int id;
	
	/**
	 * The date the sale was made 
	 */
	private final Date date;
	
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
	private final int customerId;
			
	/**
	 * A pair of constants to map the status of a sale to a string
	 */
	private static final String CLOSED = "C";
	private static final String OPEN = "O";

	
	// 1. constructor 
	
	/**
	 * Creates a new sale given the customer Id and the date it occurs.
	 * 
	 * @param customerId The customer Id the sale belongs to
	 * @param date The date the sale took place
	 */
	public SaleRowDataGateway(int customerId, Date date) {
		id = nextId++;
		this.date = date;
                // FIXME: (?) overridable method call in constructor.
		setStatus(SaleStatus.OPEN);
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
	 * @return The sale's Id
	 */
	public int getId() {
		return id;
	}

	
	// 3. interaction with the repository (a memory map in this simple example)
	
	/**
	 * Stores the information in the repository
	 */
	public void insert () {		
		sales.put(id, this);
	}
	
	
	/**
	 * Fetches a sale with a given saleId and returns a sale row gateway object, 
	 * in case it exists (Optional). 
	 * 
	 * @param id The id of the sale to find in the repository
	 * @return A sale row gateway object with the sale whose id matches saleId,
	 * in case it exists; otherwise it returns an empty optional value.
	 */
	public static Optional<SaleRowDataGateway> find (int id) {
		return Optional.ofNullable(sales.get(id));
	}
}
