package dataaccess;


/**
 * The top level exception for the persistence layer.
 * 
 * In this simple example the only persistence exception is the
 * RecordNotFoundException. In more complex examples, other classes
 * would subclass this class. 
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public class PersistenceException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public PersistenceException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public PersistenceException(String message, Exception e) {
		super (message, e);
	}
}
