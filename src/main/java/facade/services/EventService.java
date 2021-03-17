package facade.services;

import java.util.Date;

import business.EventTransactionScripts;
import facade.exceptions.ApplicationException;

public enum EventService {
	INSTANCE;

	
	private EventTransactionScripts eventTS;
	
	private EventService() {
		eventTS = new EventTransactionScripts();
	}
	
	
	public void createEvent(String name,Date date,String place,double price) throws ApplicationException {
		eventTS.createEvent(name,date,place,price);
	}
	
	
}
