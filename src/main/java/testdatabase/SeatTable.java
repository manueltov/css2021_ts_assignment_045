package testdatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.SeatRowDataGateway;

public class SeatTable {

	private HashMap<Integer,SeatRowDataGateway> seats; 
	private static SeatTable singleton = null;
	private int nextID;
	
	
	private SeatTable(){
		seats = new HashMap<>();
		nextID = 0;
	}
	
	public static SeatTable getInstance() {
		if(singleton == null) {
			singleton = new SeatTable();
		}
		return singleton;
	}
	
	public void createRow(SeatRowDataGateway row) {
		seats.put(nextID,row);
		row.setSeatID(nextID);
		nextID++;
	}
	
	public SeatRowDataGateway getByID(int id) {
		return seats.get(id);
	}

	public Integer[] getSeatsIDsOfPlace(int placeID) {
		List<Integer> ids = new ArrayList<Integer>();
		for (SeatRowDataGateway row : seats.values()) {
			if(row.getPlaceID() == placeID) {
				ids.add(row.getSeatID());
			}
		}
		Integer[] aux = new Integer[ids.size()];
		return ids.toArray(aux);
	}
}
