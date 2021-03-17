package dbutils;

import java.io.IOException;
import java.sql.SQLException;

import dataaccess.DataSource;
import dataaccess.PersistenceException;
import dataaccess.PlaceRowDataGateway;
import dataaccess.SeatRowDataGateway;
import datatypes.SeatType;

public class InitializeDatabase {
	
	private InitializeDatabase(){}


	public static void init() {
		try {
			
			RunSQLScript.runScript(DataSource.INSTANCE.getConnection(), "data/scripts/createDDL-Derby.sql");
			
			PlaceRowDataGateway estadio = new PlaceRowDataGateway("Estadio Futebol Animado");
			estadio.insert();
			int estadio_id = estadio.getPlaceID();
			for (int i = 1; i <= 5; i++) {
				SeatRowDataGateway s = new SeatRowDataGateway(estadio_id, "A", i, SeatType.VIP);
				s.insert();

			}
			for (int j = 1; j <= 10; j++) {
				SeatRowDataGateway s = new SeatRowDataGateway(estadio_id, "B", j, SeatType.NORMAL);
				s.insert();

			}
			PlaceRowDataGateway pavilhao = new PlaceRowDataGateway("Pavilhao Desportivo Alegre");
			pavilhao.insert();
			int pavilhao_id = pavilhao.getPlaceID();
			SeatRowDataGateway s = new SeatRowDataGateway(pavilhao_id, "A", 1, SeatType.VIP);
			s.insert();
			s = new SeatRowDataGateway(pavilhao_id, "A", 2, SeatType.VIP);
			s.insert();
			s = new SeatRowDataGateway(pavilhao_id, "B", 1, SeatType.NORMAL);
			s.insert();
			s = new SeatRowDataGateway(pavilhao_id, "B", 2, SeatType.NORMAL);
			s.insert();
			s = new SeatRowDataGateway(pavilhao_id, "B", 3, SeatType.NORMAL);
			s.insert();
			
		}catch (PersistenceException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void dropTables() {
		try {
			RunSQLScript.runScript(DataSource.INSTANCE.getConnection(), "data/scripts/resetTables-Derby.sql");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
