package dataaccess;

import java.time.LocalDate;

public class ReserveRowDataGateway {

	private String place;
	private LocalDate date;
	
	public ReserveRowDataGateway(String place,LocalDate date) {
		this.place = place;
		this.date = date;
	}

	public static boolean haveReserve(String place, LocalDate date) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getPlace() {
		return place;
	}
	public LocalDate getDate() {
		return date;
	}
	
	
}
