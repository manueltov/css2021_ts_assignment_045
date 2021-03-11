package testdatabase;

import java.util.HashMap;

import dataaccess.PlaceRowDataGateway;

public class PlaceTable {

	
	private HashMap<String,PlaceRowDataGateway> places; 
	private static PlaceTable singleton = null;
	
	private PlaceTable() {
		places = new HashMap<>();
	}
	
	public static PlaceTable getInstance() {
		if(singleton == null) {
			singleton = new PlaceTable();
		}
		return singleton;
	}
	
	public void createRow(PlaceRowDataGateway row) {
		places.put(row.getPlace(),row);
	}
	
	public PlaceRowDataGateway getRow(String name) {
		return places.get(name);
	}
}
