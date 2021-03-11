package dataaccess;

import java.util.Optional;

import testdatabase.PlaceTable;

public class PlaceRowDataGateway {
	
	private static PlaceTable table = PlaceTable.getInstance();
	private String place;
	private int placeID;
	
	public PlaceRowDataGateway(String place,int numberOfSeats,int numberOfRows) {
		this.place = place;
		placeID = -1;
	}

	public static Optional<PlaceRowDataGateway> findPlaceByName(String place) {
		return Optional.ofNullable(table.getRowByName(place));
	}
	
	public static Optional<PlaceRowDataGateway> findPlaceByID(int id) {
		return Optional.ofNullable(table.getRowByID(id));
	}
	
	public void insert() {
		table.createRow(this);
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
