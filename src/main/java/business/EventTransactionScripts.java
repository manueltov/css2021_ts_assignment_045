package business;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dataaccess.EventRowDataGateway;
import dataaccess.PlaceRowDataGateway;
import dataaccess.TicketRowDataGateway;
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
		
		
		List<Ticket> tickets = p.get().getTickets();
		TicketRowDataGateway[] trdg = new TicketRowDataGateway[tickets.size()];
		
		
		EventRowDataGateway ev = new EventRowDataGateway(name,date,p,price);
		for (int i = 0; i < trdg.length; i++) {
			trdg[i] = new TicketRowDataGateway(tickets.get(i),ev);
			trdg[i].insert();
		}
		ev.setTickets(trdg);
		ev.insert();
		
	}

	private boolean checkPrice(double price) {
		return price > 0;
	}

}
