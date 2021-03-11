package testdatabase;

import java.time.LocalDate;
import java.util.HashMap;

import dataaccess.ReserveRowDataGateway;

public class ReserveTable {


	private HashMap<String,ReserveRowDataGateway> reserves; 
	private static ReserveTable singleton = null;
	
	private ReserveTable() {
		reserves = new HashMap<>();
	}
	
	public static ReserveTable getInstance() {
		if(singleton == null) {
			singleton = new ReserveTable();
		}
		return singleton;
	}
	
	public void createRow(ReserveRowDataGateway row) {
		reserves.put(row.getPlace(),row);
	}
	
	public ReserveRowDataGateway getRow(String name) {
		return reserves.get(name);
	}

	public boolean haveReserve(String place, LocalDate date) {
		for(ReserveRowDataGateway row : reserves.values()) {
			if(row.getPlace().contentEquals(place)) {
				if(row.getDate().equals(date)) {
					return true;
				}
			}
		}
		return false;
	}
}
