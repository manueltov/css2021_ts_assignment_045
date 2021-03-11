package client;

import java.time.LocalDate;

import facade.exceptions.ApplicationException;
import facade.services.EventService;

/**
 * A simple application client that uses both services.
 *	
 * @author fmartins
 * @version 1.2 (11/02/2015)
 * 
 */
public final class SimpleClient {

	private SimpleClient() {
		// an utility class
	}
	
	/**
	 * A simple interaction with the application services
	 * 
	 * @param args Command line parameters
	 */
	public static void main(String[] args) {

		
		//TESTE
		EventService es = EventService.INSTANCE;
		
		try {
			es.createEvent("Evento", LocalDate.now(), "", 2.0);
		}catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}
	
		
	}
}
