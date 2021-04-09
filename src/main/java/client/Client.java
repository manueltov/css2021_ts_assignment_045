package client;


import dbutils.InitializeDatabase;
import facade.startup.EventSys;

public class Client {

	public static void main(String[] args) {

		EventSys app = new EventSys();
		try {
			app.run();
			InitializeDatabase.dropTables(); // DROP
			InitializeDatabase.init(); // INIT
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
