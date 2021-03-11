package dataaccess;


public class TicketRowDataGateway {
	
	private int eventID;
	private char row;
	private int placeID;
	private int seatID;
	private int number;
	private boolean available;
	
	public TicketRowDataGateway(int eventID,int place,int seatID) {
		this.placeID = place;
		this.eventID = eventID;
		this.available = true;
		this.seatID = seatID;
	}
	

	public int getNumber() {
		return number;
	}
	
	public char getRow() {
		return row;
	}

	public void insert() {
		// TODO Auto-generated method stub
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public int getSeatID() {
		return seatID;
	}
	
	public int getEventID() {
		return eventID;
	}
	public int getPlaceID() {
		return placeID;
	}
	 
	
	
	
	

}
