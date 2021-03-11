package dataaccess;

import java.time.LocalDate;
import java.util.Optional;

import testdatabase.EventTable;



public class EventRowDataGateway {
	
		
	private static EventTable table = EventTable.getInstance();
	private String name;
	private LocalDate date;
	private String place;
	private double price;
	private int eventID;

	public EventRowDataGateway(String name, LocalDate date, String place, double price) {
		this.name = name;
		this.date = date;
		this.place = place;
		this.price = price;
		this.eventID = -1;
	}

	public void insert() {
		table.createRow(this);		
	}

	public static Optional<EventRowDataGateway> findEventByName(String name) {
		return Optional.ofNullable(table.getRowByName(name));
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public LocalDate getDate() {
		return date;
	}
	public String getPlace() {
		return place;
	}
	
	@Override
	public String toString() {
		return name+" | "+place+" | "+date.toString()+" | Ticket Price:"+price;
	}

	public static boolean haveReserve(String place, LocalDate date) {
		return table.haveReserve(place,date);
	}

	public int getEventID() {
		return eventID;
	}
	
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	
	
	

}
