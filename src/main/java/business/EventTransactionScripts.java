package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dataaccess.EventRowDataGateway;
import dataaccess.PlaceRowDataGateway;
import datatypes.Ticket;
import facade.exceptions.ApplicationException;

public class EventTransactionScripts {

	public void createEvent(String name, LocalDate date, String place, double price) throws ApplicationException {
		if(name.trim().length() == 0) {
			throw new ApplicationException("Event name not valid.");
		}
		if(EventRowDataGateway.findEventByName(name).isPresent()) {
			throw new ApplicationException("Event already exists.");
		}
		
		Optional<PlaceRowDataGateway> p = PlaceRowDataGateway.findPlaceByName(place);
		if(!p.isPresent()) {
			throw new ApplicationException("Place not found.");
		}
		
		if(!PlaceRowDataGateway.isAvailable(place,date)) {
			throw new ApplicationException("Place not avaiable.");
		}
		
		if(!checkPrice(price)) {
			throw new ApplicationException("Invalid price, price can not be 0 or less.");
		}
		
		
		List<Ticket> tickets = p.getTickets();
		TicketRowDataGateway[] trdg = new ArrayList<TicketRowDataGateway>();
		
		
		EventRowDataGateway ev = new EventRowDataGateway(name,date,p,price);
		for (Ticket t : tickets) {
			trdg.add(new TicketRowDataGateway(ev,price));
		}
		ev.setTickets(trdg);
		ev.insert();
		
		
		
		
		
		
	}

	private boolean checkPrice(double price) {
		return price > 0;
	}

}
