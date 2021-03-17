package facade.services;

import business.SaleTransactionScripts;
import facade.exceptions.ApplicationException;

/**
 * A sale service. The purpose of this class is to provide access to the business
 * layer, hiding its implementation from the clients. Clients should never interact 
 * directly with the business layer to, so this very simple class offers methods 
 * to add new sales, add products to the sales, and to obtain sale discounts. 
 * 
 * One difference from v0 is that the SaleService is no longer a singleton. 
 * The use of singletons should be avoided as much as possible, since it introduces a 
 * "hard" dependence between clients classes and the name of the Singleton class. 
 * This, for instance, makes testing more difficult, but we will not expand on it in 
 * this course.
 * 
 * As you will see in future, different implementation of the
 * business and data access layer will not change this interface and, consequently, 
 * the way clients interact with the application.
 * 
 * @author fmartins
 * @version 1.1 (16/2/2017)
 */
public class SaleService {

    private SaleTransactionScripts saleTS;

    public SaleService(SaleTransactionScripts saleTS) {
        this.saleTS = saleTS;
    }

    public int newSale(int vat) throws ApplicationException {
        return saleTS.newSale(vat);
    }

    public void addProductToSale(int saleId, int productCode, int qty)
            throws ApplicationException {
        saleTS.addProductToSale(saleId, productCode, qty);
    }

    public double getSaleDiscount(int saleId) throws ApplicationException {
        return saleTS.getSaleDiscount(saleId);
    }
}