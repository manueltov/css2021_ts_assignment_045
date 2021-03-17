package dbutils;

import java.io.IOException;
import java.sql.SQLException;

import dataaccess.DataSource;
import dataaccess.PersistenceException;
import facade.startup.SaleSys;

public class CreateDatabase {

    public void createCSSDerbyDB() throws IOException, SQLException, PersistenceException {
        DataSource.INSTANCE.connect(SaleSys.DB_CONNECTION_STRING + ";create=true", "SaleSys", "");
        RunSQLScript.runScript(DataSource.INSTANCE.getConnection(), "data/scripts/createDDL-Derby.sql");
        DataSource.INSTANCE.close();
    }

    public static void main(String[] args) throws PersistenceException, IOException, SQLException {
        new CreateDatabase().createCSSDerbyDB();
    }

}
