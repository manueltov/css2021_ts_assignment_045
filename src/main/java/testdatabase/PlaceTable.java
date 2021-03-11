package testdatabase;

import java.util.HashMap;

import dataaccess.PlaceRowDataGateway;

public class PlaceTable {

	
	private HashMap<Integer,PlaceRowDataGateway> places; 
	private static PlaceTable singleton = null;
	private int nextID;
	
	private PlaceTable() {
		places = new HashMap<>();
		nextID = 0;
	}
	
	public static PlaceTable getInstance() {
		if(singleton == null) {
			singleton = new PlaceTable();
		}
		return singleton;
	}
	
	public void createRow(PlaceRowDataGateway row) {
		places.put(nextID,row);
		row.setPlaceID(nextID);
		nextID++;
	}
	
	public PlaceRowDataGateway getRowByName(String name) {
		for (PlaceRowDataGateway place : places.values()) {
			if(place.getPlace().contentEquals(name)) {
				return place;
			}
		}
		return null;
	}
	
	public PlaceRowDataGateway getRowByID(int id) {
		return places.get(id);
	}
}
