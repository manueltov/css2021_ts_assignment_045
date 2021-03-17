package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;



public class EventRowDataGateway {
	

    /**
     * Table name
     */
    private static final String TABLE_NAME = "event";

    /**
	 * Column labels
     */
    private static final String ID_COLUMN_NAME = "id";
    private static final String DATE_COLUMN_NAME = "data";
    private static final String PLACE_ID_COLUMN_NAME = "place";
    private static final String PRICE_TOTAL_COLUMN_NAME = "price";
    private static final String NAME_COLUMN_NAME = "name";


	
	
	// SQL statements

	/**
	 * insert an event
	 */
	private static final String INSERT_EVENT_SQL =
			"insert into " + TABLE_NAME + " (" + DATE_COLUMN_NAME + ", " + PLACE_ID_COLUMN_NAME + ", " + 
					PRICE_TOTAL_COLUMN_NAME + ", " + NAME_COLUMN_NAME +") " +
			"values (?, ?, ?, ?)";

	/**
	 * obtain a event by Id
	 */
	private static final String GET_EVENT_BY_ID_SQL = 
			"select " + ID_COLUMN_NAME + ", " + DATE_COLUMN_NAME + ", " + PLACE_ID_COLUMN_NAME + ", " + 
					PRICE_TOTAL_COLUMN_NAME + ", " + NAME_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
            "where " + ID_COLUMN_NAME + " = ?";
	

	private static final String GET_EVENT_BY_NAME_SQL = 
			"select " + ID_COLUMN_NAME + ", " + DATE_COLUMN_NAME + ", " + PLACE_ID_COLUMN_NAME + ", " + 
					PRICE_TOTAL_COLUMN_NAME + ", " + NAME_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
            "where " + NAME_COLUMN_NAME + " = ?";
	
		
	private String name;
	private java.sql.Date date;
	private int placeID;
	private double price;
	private int eventID;

	public EventRowDataGateway(String name, Date date, int placeID, double price) {
		this.name = name;
		this.date = new java.sql.Date(date.getTime());
		this.placeID = placeID;
		this.price = price;
		this.eventID = -1;
	}

	public void insert() throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_EVENT_SQL)) {
			// set statement arguments
			statement.setDate(1, date);
			statement.setInt(2, placeID);
			statement.setDouble(3, price);
			statement.setString(4, name);
			// execute SQL
			statement.executeUpdate();
			// Gets sale Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next(); 
				eventID = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new sale!", e);
		}	
	}
	
	public static Optional<EventRowDataGateway> find(int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_EVENT_BY_ID_SQL)) {
			// set statement arguments
			statement.setInt(1, id);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer with the data retrieved from the database
				return Optional.of(loadSale(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			//log.log(Level.SEVERE, "Internal error getting a sale", e);
			return Optional.empty();
		}
	}

	public static Optional<EventRowDataGateway> findEventByName(String name) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_EVENT_BY_NAME_SQL)) {
			// set statement arguments
			statement.setString(1, name);
			// execute SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer with the data retrieved from the database
				return Optional.of(loadSale(rs));
			}
		} catch (SQLException | PersistenceException e) {
			// log the exception so we can understand the problem in finer detail
			//log.log(Level.SEVERE, "Internal error getting a sale", e);
			return Optional.empty();
		}
	}

	private static EventRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			EventRowDataGateway row = new EventRowDataGateway(rs.getString(NAME_COLUMN_NAME),
					rs.getDate(DATE_COLUMN_NAME), rs.getInt(PLACE_ID_COLUMN_NAME),rs.getDouble(PRICE_TOTAL_COLUMN_NAME));
			return row;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Sale does not exist	", e);
		}
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public Date getDate() {
		return date;
	}
	public int getPlaceID() {
		return placeID;
	}
	
	@Override
	public String toString() {
		return name+" | "+placeID+" | "+date.toString()+" | Ticket Price:"+price;
	}

	public static boolean haveReserve(String place, Date date2) {
		return true;
	}

	public int getEventID() {
		return eventID;
	}
	
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	
	
	

}
