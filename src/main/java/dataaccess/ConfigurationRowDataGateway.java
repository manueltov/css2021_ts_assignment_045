package dataaccess;

/**
 * An in-memory representation of a row gateway for a configuration row.
 * It is a singleton, since there is only one configuration row.
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public enum ConfigurationRowDataGateway {
	INSTANCE;

	// Configuration attributes 
	
	private final double totalAmountPercentage;
	private final double amountThreshold;
	private final double eligiblePercentage;
	
	
	// 1. constructor 
	
	private ConfigurationRowDataGateway() {
		totalAmountPercentage = 0.1;
		amountThreshold = 50;
		eligiblePercentage = 0.15;
	}

	
	// 2. getters. There is no setters, since the application 
	// does not support the change of these attributes. 
	// (Of course, only an illustrative example).

	/**
	 * @return The discount percentage to apply for the total sale
	 */
	public double getAmountThresholdPercentage() {
		return totalAmountPercentage;
	}

	/**
	 * @return The sale's amount threshold for being eligible for a
	 * total sale discount.
	 */
	public double getAmountThreshold() {
		return amountThreshold;
	}

	/**
	 * @return The discount percentage to apply to products marked
	 * as eligible.
	 */
	public double getEligiblePercentage() {
		return eligiblePercentage;
	}

	
	// 3. interaction with the repository. In this case it always 
	// returns the same configuration row, since there is only one
	// available.

	public static ConfigurationRowDataGateway loadConfiguration () {
		return INSTANCE;
	}
}
