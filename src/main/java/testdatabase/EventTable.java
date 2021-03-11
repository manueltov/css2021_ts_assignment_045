package testdatabase;

import java.time.LocalDate;
import java.util.HashMap;

import dataaccess.EventRowDataGateway;

public class EventTable {

	private HashMap<Integer,EventRowDataGateway> events; 
	private static EventTable singleton = null;
	private int nextID;
	private EventTable() {
		events = new HashMap<>();
		nextID = 0;
	}
	
	public static EventTable getInstance() {
		if(singleton == null) {
			singleton = new EventTable();
		}
		return singleton;
	}
	
	public void createRow(EventRowDataGateway row) {
		events.put(nextID,row);
		row.setEventID(nextID);
		nextID++;
	}
	
	public EventRowDataGateway getRowByName(String name) {
		for (EventRowDataGateway ev : events.values()) {
			if(ev.getName().contentEquals(name)) {
				return ev;
			}
		}
		return null;
	}
	
	public EventRowDataGateway getRowByID(int id) {
		return events.get(id);
	}


	public boolean haveReserve(String place, LocalDate date) {
		for (EventRowDataGateway ev : events.values()) {
			if(ev.getPlace().contentEquals(place)) {
				if(ev.getDate().equals(date)){
					return true;
				}
			}
		}
		return false;
	}
}
