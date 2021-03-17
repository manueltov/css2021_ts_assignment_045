package facade.startup;

import dataaccess.DataSource;
import dataaccess.PersistenceException;
import facade.exceptions.ApplicationException;
import facade.services.EventService;

public class EventSys {
	  private EventService eventService;

	    public static final String DB_CONNECTION_STRING = "jdbc:derby:data/newderby/db";
	    
	    public void run() throws ApplicationException {
	        // Connects to the database
	        try {
	            DataSource.INSTANCE.connect(DB_CONNECTION_STRING + ";create=false", "EventSys", "");
	            eventService = EventService.INSTANCE;
	        } catch (PersistenceException e) {
	            throw new ApplicationException("Error connecting database", e);
	        }
	    }

	    public void stopRun() {
	        // Closes the database connection
	        DataSource.INSTANCE.close();
	    }

	    public EventService getEventService() {
			return eventService;
		}
	    
}
