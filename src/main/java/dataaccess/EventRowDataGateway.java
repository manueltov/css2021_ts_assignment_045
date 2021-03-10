package dataaccess;

import java.time.LocalDate;
import java.util.Optional;



public class EventRowDataGateway {
	
	
	
	private String name;
	private LocalDate date;
	private String place;
	private double price;

	public EventRowDataGateway(String name, LocalDate date, String place, double price) {
		this.name = name;
		this.date = date;
		this.place = place;
		this.price = price;
	}

	public void insert() {
		// TODO Auto-generated method stub
		
	}

	public static Optional<EventRowDataGateway> findEventByName(String name) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(null);
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
	

}
