package facade.startup;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import business.handlers.NewEventHandler;
import facade.exceptions.ApplicationException;
import facade.services.EventService;

public class EventSys {
	  private EventService eventService;
	  private EntityManagerFactory emf;

	    public static final String DB_CONNECTION_STRING = "jdbc:derby:data/newderby/db";
	    
	    public void run() throws ApplicationException {
	    	// Connects to the database
			try {
				emf = Persistence.createEntityManagerFactory("domain-model-jpa");
				eventService = new EventService(new NewEventHandler(emf));
				// exceptions thrown by JPA are not checked
			} catch (Exception e) {
				throw new ApplicationException("Error connecting database", e);
			}
	    }

	    public void stopRun() {
	        // Closes the database connection
	        emf.close();
	    }

	    public EventService getEventService() {
			return eventService;
		}
	    
}
