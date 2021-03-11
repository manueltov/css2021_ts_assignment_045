package dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import datatypes.Ticket;
import testdatabase.PlaceTable;

public class PlaceRowDataGateway {
	
	private static PlaceTable table = PlaceTable.getInstance();
	private String place;
	private int numberOfSeats;
	private int numberOfRows;
	
	public PlaceRowDataGateway(String place,int numberOfSeats,int numberOfRows) {
		this.place = place;
		this.numberOfRows = numberOfRows;
		this.numberOfSeats = numberOfSeats;
	}

	public static Optional<PlaceRowDataGateway> findPlaceByName(String place) {
		return Optional.ofNullable(table.getRow(place));
	}
	
	public void insert() {
		table.createRow(this);
	}

	public Ticket[] getTickets() {
		List<Ticket> aux = new ArrayList<>();
		int r = (int) 'A';
		int s = 1;
		while(r - ((int) 'A') <= numberOfSeats) {
			while(s <= numberOfRows) {
				aux.add(new Ticket((char) r, s, place));
				r++;
				s++;
			}
			s = 1;
		}
		Ticket[] rt = new Ticket[aux.size()];
		return aux.toArray(rt);
	}

	public String getPlace() {
		// TODO Auto-generated method stub
		return place;
	}

}
