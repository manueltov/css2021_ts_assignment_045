package client;

import java.util.Date;

import dbutils.InitializeDatabase;
import facade.services.EventService;
import facade.startup.EventSys;

public class Client {

	public static void main(String[] args) {

		EventSys app = new EventSys();
		try {
			app.run();
			InitializeDatabase.dropTables(); // DROP
			InitializeDatabase.init(); // INIT

			EventService eventService = app.getEventService();

			try {
				eventService.createEvent("Place not found", new Date(), "Estadio estadio estadio", 2.0);
			}catch (Exception e) {
				System.out.println("Test Place not found: Passed");
			}
			boolean passed = false;
			try {
				eventService.createEvent("Sucesso", new Date(), "Estadio Futebol Animado", 2.0);
				passed = true;
			}catch (Exception e) {
				System.out.println("Test Sucesso: Failed");
				passed = false;
			}
			if(passed)
				System.out.println("Test Sucesso: Passed");
			try {
				eventService.createEvent("Ocupado", new Date(), "Estadio Futebol Animado", 2.0);
			}catch (Exception e) {
				System.out.println("Test Ocupado: Passed");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
