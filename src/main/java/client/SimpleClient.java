package client;

import java.time.LocalDate;

import dataaccess.PlaceRowDataGateway;
import facade.exceptions.ApplicationException;
import facade.services.EventService;
import testdatabase.PlaceTable;

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

		PlaceTable.getInstance().createRow(new PlaceRowDataGateway("Pavilhao 1", 2, 2));
//		ReserveTable.getInstance().createRow(new ReserveRowDataGateway("Pavilhao 1", LocalDate.now()));
//		EventTable.getInstance().createRow(new EventRowDataGateway("Evento", LocalDate.now().minusDays(1), "Pavilhao", 2.0));

		
		//TESTE
		EventService es = EventService.INSTANCE;
		
		try {
			es.createEvent("Evento", LocalDate.now(), "Pavilhao 1", 2.0);
		}catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}
	
		
	}
}
