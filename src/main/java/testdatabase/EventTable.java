package testdatabase;

import java.util.HashMap;

import dataaccess.EventRowDataGateway;

public class EventTable {

	private HashMap<String,EventRowDataGateway> events; 
	private static EventTable singleton = null;
	
	private EventTable() {
		events = new HashMap<>();
	}
	
	public static EventTable getInstance() {
		if(singleton == null) {
			singleton = new EventTable();
		}
		return singleton;
	}
	
	public void createRow(EventRowDataGateway row) {
		events.put(row.getName(),row);
	}
	
	public EventRowDataGateway getRow(String name) {
		return events.get(name);
	}
}
