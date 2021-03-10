package testdatabase;

import java.util.HashMap;

import dataaccess.EventRowDataGateway;

public class PlaceTable {

	
	private HashMap<String,EventRowDataGateway> places; 
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
	
	public void createRow(EventRowDataGateway row) {
		places.put(row.getName(),row);
	}
	
	public EventRowDataGateway getRow(String name) {
		return places.get(name);
	}
}
