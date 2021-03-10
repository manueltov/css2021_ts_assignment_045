package datatypes;

public class Ticket {

	private int number;
	private String row;
	private String place;
	
	public Ticket(String row,int number,String place) {
		this.row = row;
		this.number = number;
		this.place = place;
	}
	
	public String getRow() {
		return row;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getPlace() {
		return place;
	}
	
	
}
