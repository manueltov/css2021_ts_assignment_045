package dataaccess;

import datatypes.SeatType;
import testdatabase.SeatTable;

public class SeatRowDataGateway {

	private static SeatTable table = SeatTable.getInstance();
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
	
	public static SeatRowDataGateway getByID(int id) {
		return table.getByID(id);
	}
	
	public void insert()
	{
		table.createRow(this);
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
		return table.getSeatsIDsOfPlace(placeID);
	}
	
	 
	 
}
