package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class PlaceRowDataGateway {
	

    /**
     * Table name
     */
    private static final String TABLE_NAME = "place";

    /**
	 * Column labels
     */
    private static final String ID_COLUMN_NAME = "id";
    private static final String PLACE_COLUMN_NAME = "place";

	
	// SQL statements

	/**
	 * insert a sale
	 */
	private static final String INSERT_PLACE_SQL =
			"insert into " + TABLE_NAME + " (" + PLACE_COLUMN_NAME + ") " +
			"values (?)";

	/**
	 * obtain a sale by Id
	 */
	private static final String GET_PLACE_BY_ID_SQL = 
			"select " + ID_COLUMN_NAME + ", " + PLACE_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
            "where " + ID_COLUMN_NAME + " = ?";
	
	private static final String GET_PLACE_BY_NAME_SQL = 
			"select " + ID_COLUMN_NAME + ", " + PLACE_COLUMN_NAME + " " +
			"from " + TABLE_NAME + " " +
            "where " + PLACE_COLUMN_NAME + " = ?";
	
	private String place;
	private int placeID;
	
	public PlaceRowDataGateway(String place) {
		this.place = place;
		placeID = -1;
	}

	public static Optional<PlaceRowDataGateway> findPlaceByName(String place) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_PLACE_BY_NAME_SQL)) {
			// set statement arguments
			statement.setString(1, place);
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
	
	public static Optional<PlaceRowDataGateway> findPlaceByID(int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_PLACE_BY_ID_SQL)) {
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
	
	private static PlaceRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			PlaceRowDataGateway row = new PlaceRowDataGateway(rs.getString(PLACE_COLUMN_NAME));
			return row;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Sale does not exist	", e);
		}
	}

	public void insert() throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_PLACE_SQL)) {
			// set statement arguments
			statement.setString(1, place);
			// execute SQL
			statement.executeUpdate();
			// Gets sale Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next(); 
				placeID = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new sale!", e);
		}
	}


	public String getPlace() {
		return place;
	}
	
	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}
	public int getPlaceID() {
		return placeID;
	}
	

}
