package facade.services;

import java.util.Date;

import business.EventTransactionScripts;
import business.NewEventHandler;
import facade.exceptions.ApplicationException;

public class EventService {

	private NewEventHandler newEventhandler;
	public EventService(NewEventHandler newEventhandler) {
		this.newEventhandler = newEventhandler;
	}
	
	public void createEvent() {
		
	}
	
	public void createEvent(String name,Date date,String place,double price) throws ApplicationException {
		//newEventhandler.createEvent(name,date,place,price);
	}
	
	
}
