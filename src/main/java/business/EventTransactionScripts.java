package business;

import java.util.Date;
import java.util.Optional;

import dataaccess.EventRowDataGateway;
import dataaccess.PersistenceException;
import dataaccess.PlaceRowDataGateway;
import dataaccess.SeatRowDataGateway;
import dataaccess.TicketRowDataGateway;
import facade.exceptions.ApplicationException;

public class EventTransactionScripts {

	public void createEvent(String name, Date date, String place, double price) throws ApplicationException {
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
		
		if(EventRowDataGateway.haveReserve(place,date)) {
			throw new ApplicationException("Place not avaiable.");
		}
		
		if(!checkPrice(price)) {
			throw new ApplicationException("Invalid price, price can not be 0 or less.");
		}
		
		
		Integer[] tickets = SeatRowDataGateway.getIDsOfPlace(p.get().getPlaceID());
		TicketRowDataGateway[] trdg = new TicketRowDataGateway[tickets.length];
		EventRowDataGateway ev = new EventRowDataGateway(name,date,p.get().getPlaceID(),price);
		for (int i = 0; i < trdg.length; i++) {
			trdg[i] = new TicketRowDataGateway(ev.getEventID(),p.get().getPlaceID(),tickets[i]);
			if(trdg[i].getEventID() == -1 || trdg[i].getPlaceID() == -1 || trdg[i].getSeatID() == -1) {
				throw new ApplicationException("Error while creating tickets.");
			}
			try {
				trdg[i].insert();
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			ev.insert();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private boolean checkPrice(double price) {
		return price > 0;
	}

}
