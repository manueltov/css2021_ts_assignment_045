package facade.startup;

import business.CustomerTransactionScripts;
import business.SaleTransactionScripts;
import dataaccess.DataSource;
import dataaccess.PersistenceException;
import facade.exceptions.ApplicationException;
import facade.services.CustomerService;
import facade.services.SaleService;

public class SaleSys {

    private CustomerService customerService;
    private SaleService saleService;

    public static final String DB_CONNECTION_STRING = "jdbc:derby:data/derby/cssdb";
    
    public void run() throws ApplicationException {
        // Connects to the database
        try {
            DataSource.INSTANCE.connect(DB_CONNECTION_STRING + ";create=false", "SaleSys", "");
            customerService = new CustomerService(new CustomerTransactionScripts());
            saleService = new SaleService(new SaleTransactionScripts());
        } catch (PersistenceException e) {
            throw new ApplicationException("Error connecting database", e);
        }
    }

    public void stopRun() {
        // Closes the database connection
        DataSource.INSTANCE.close();
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public SaleService getSaleService() {
        return saleService;
    }
}
 