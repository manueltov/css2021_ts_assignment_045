package dataaccess;

import java.time.LocalDate;
import java.util.Optional;

import datatypes.Ticket;

public class PlaceRowDataGateway {
	
	private String place;
	
	public PlaceRowDataGateway(String place) {
		this.place = place;
	}

	public static Optional<PlaceRowDataGateway> findPlaceByName(String place) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isAvailable(String place, LocalDate date) {
		// TODO Auto-generated method stub
		return false;
	}

	public Ticket[] getTickets() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlace() {
		// TODO Auto-generated method stub
		return place;
	}

}
