package dbutils;

import java.io.IOException;
import java.sql.SQLException;

import dataaccess.DataSource;
import dataaccess.PersistenceException;
import facade.startup.SaleSys;

public class ResetTables {

    public void resetCSSDerbyDB() throws IOException, SQLException, PersistenceException {
        DataSource.INSTANCE.connect(SaleSys.DB_CONNECTION_STRING + ";create=false", "SaleSys", "");
        RunSQLScript.runScript(DataSource.INSTANCE.getConnection(), "data/scripts/resetTables-Derby.sql");
        DataSource.INSTANCE.close();
    }

    public static void main(String[] args) throws PersistenceException, IOException, SQLException {
        new ResetTables().resetCSSDerbyDB();
    }

}
