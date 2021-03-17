package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class TicketRowDataGateway {
	
	/**
	 * Table name
	 */
	private static final String TABLE_NAME = "seat";

	/**
	 * Column labels
	 */
	private static final String ID_COLUMN_NAME = "id";
	private static final String PLACE_ID_COLUMN_NAME = "place_id";
	private static final String EVENT_ID_COLUMN_NAME = "event_id";
	private static final String SEAT_ID_COLUMN_NAME = "seat_id";


	/**
	 * insert a sale
	 */
	private static final String INSERT_TICKET_SQL =
			"insert into " + TABLE_NAME + " (" + PLACE_ID_COLUMN_NAME + ", "+ EVENT_ID_COLUMN_NAME + ", " + SEAT_ID_COLUMN_NAME +") " +
					"values (?, ?,?)";

	/**
	 * obtain a sale by Id
	 */
	private static final String GET_TICKET_SQL = 
			"select " + ID_COLUMN_NAME + ", " + PLACE_ID_COLUMN_NAME + ", "+ EVENT_ID_COLUMN_NAME + ", " + SEAT_ID_COLUMN_NAME + " " +
					"from " + TABLE_NAME + " " +
					"where " + ID_COLUMN_NAME + " = ?";
	
	private static final String SOLD = "S";
	private static final String AVAILABLE = "A";
	
	private int ticketID;
	private int eventID;
	private int placeID;
	private int seatID;
	private String status;
	
	public TicketRowDataGateway(int eventID,int place,int seatID) {
		this.placeID = place;
		this.eventID = eventID;
		this.status = AVAILABLE;
		this.seatID = seatID;
		this.ticketID = -1;
	}
	
	
	
	public String getStatus() {
		return status;
	}
	
	public static Optional<TicketRowDataGateway> getByID(int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_TICKET_SQL)) {
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

	private static TicketRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			TicketRowDataGateway row = new TicketRowDataGateway(rs.getInt(EVENT_ID_COLUMN_NAME),rs.getInt(PLACE_ID_COLUMN_NAME),
					rs.getInt(SEAT_ID_COLUMN_NAME));
			return row;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Sale does not exist	", e);
		}
	}
	

	public void insert() throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_TICKET_SQL)) {
			// set statement arguments
			statement.setInt(1, placeID);
			statement.setInt(2, eventID);
			statement.setInt(3, seatID);
			// execute SQL
			statement.executeUpdate();
			// Gets sale Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next(); 
				ticketID = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new sale!", e);
		}
	}
	
	public int getSeatID() {
		return seatID;
	}
	
	public int getEventID() {
		return eventID;
	}
	public int getPlaceID() {
		return placeID;
	}
	
	public int getTicketID() {
		return ticketID;
	}
	 
	
	
	
	

}
