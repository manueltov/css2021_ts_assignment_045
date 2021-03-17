package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import datatypes.SeatType;

public class SeatRowDataGateway {

	/**
	 * Table name
	 */
	private static final String TABLE_NAME = "seat";

	/**
	 * Column labels
	 */
	private static final String ID_COLUMN_NAME = "id";
	private static final String PLACEID_COLUMN_NAME = "place";
	private static final String FILA_COLUMN_NAME = "fila";
	private static final String LUGAR_COLUMN_NAME = "lugar";
	private static final String SEAT_TYPE_COLUMN_NAME = "seat_type";


	/**
	 * insert a sale
	 */
	private static final String INSERT_SEAT_SQL =
			"insert into " + TABLE_NAME + " (" + PLACEID_COLUMN_NAME + ", " + FILA_COLUMN_NAME + ", " + 
					LUGAR_COLUMN_NAME + ", " + SEAT_TYPE_COLUMN_NAME +") " +
					"values (?, ?, ?, ?)";


	private static final String GET_SEAT_BY_ID_SQL = 
			"select " + ID_COLUMN_NAME + ", " + PLACEID_COLUMN_NAME + ", " + FILA_COLUMN_NAME + ", " + 
					LUGAR_COLUMN_NAME + ", " + SEAT_TYPE_COLUMN_NAME + " " +
					"from " + TABLE_NAME + " " +
					"where " + ID_COLUMN_NAME + " = ?";
	

	private int place;
	private String fila;
	private int lugar;
	private SeatType st;
	private int seatID;

	public SeatRowDataGateway(int placeID,String fila,int lugar,SeatType st) {
		this.place = placeID;
		this.fila = fila;
		this.lugar = lugar;
		this.st = st;
	}

	public static Optional<SeatRowDataGateway> getByID(int id) {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_SEAT_BY_ID_SQL)) {
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

	private static SeatRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			SeatRowDataGateway row = new SeatRowDataGateway(rs.getInt(ID_COLUMN_NAME),rs.getString(FILA_COLUMN_NAME),
					rs.getInt(LUGAR_COLUMN_NAME),SeatType.valueOf(rs.getString(SEAT_TYPE_COLUMN_NAME)));
			return row;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Sale does not exist	", e);
		}
	}

	public void insert() throws PersistenceException
	{
		try (PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_SEAT_SQL)) {
			// set statement arguments
			statement.setInt(1, place);
			statement.setString(2, fila);
			statement.setInt(3, lugar);
			statement.setString(4, st.toString());
			// execute SQL
			statement.executeUpdate();
			// Gets sale Id generated automatically by the database engine
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next(); 
				seatID = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException ("Internal error inserting a new sale!", e);
		}	
	}

	public int getLugar() {
		return lugar;
	}

	public String getFila() {
		return fila;
	}

	public int getPlaceID() {
		return place;
	}

	public SeatType getSeatType() {
		return st;
	}

	public void setSeatID(int id) {
		this.seatID = id;
	}

	public int getSeatID() {
		return seatID;
	}

	public static Integer[] getIDsOfPlace(int placeID) {
		return null;
	}



}
