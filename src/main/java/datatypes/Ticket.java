package datatypes;

public class Ticket {

	private int number;
	private char row;
	private String place;
	
	public Ticket(char row,int number,String place) {
		this.row = row;
		this.number = number;
		this.place = place;
	}
	
	public char getRow() {
		return row;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getPlace() {
		return place;
	}
	
	
}
