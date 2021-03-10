package datatypes;

public class Ticket {

	private int number;
	private String row;
	
	public Ticket(String row,int number) {
		this.row = row;
		this.number = number;
	}
	
	public String getRow() {
		return row;
	}
	
	public int getNumber() {
		return number;
	}
	
	
}
