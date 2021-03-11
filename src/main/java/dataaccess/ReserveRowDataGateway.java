package dataaccess;

import java.time.LocalDate;

import testdatabase.ReserveTable;

public class ReserveRowDataGateway {

	private static ReserveTable table = ReserveTable.getInstance();
	private String place;
	private LocalDate date;
	
	public ReserveRowDataGateway(String place,LocalDate date) {
		this.place = place;
		this.date = date;
	}

	public static boolean haveReserve(String place, LocalDate date) {
		return table.haveReserve(place,date);
	}
	
	public String getPlace() {
		return place;
	}
	public LocalDate getDate() {
		return date;
	}
	
	
}
