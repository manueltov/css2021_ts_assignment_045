package dataaccess;


public class TicketRowDataGateway {
	
	private String eventName;
	private String row;
	private String place;
	private int number;
	private boolean available;
	
	public TicketRowDataGateway(String eventName,String place, String row, int number) {
		this.place = place;
		this.row = row;
		this.number = number;
		this.eventName = eventName;
		this.available = true;
	}
	
	public String getPlace() {
		return place;
	}
		
	public int getNumber() {
		return number;
	}
	
	public String getRow() {
		return row;
	}

	public void insert() {
		// TODO Auto-generated method stub
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public String getEventName() {
		return eventName;
	}
		
	public boolean isAvailable() {
		return available;
	}
	

}
