package business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IsValidVATUnitTest {

	@Test
	public void isValidVATSuccess() {
		// Given
		CustomerTransactionScripts customerTS = new CustomerTransactionScripts();
		
		// When
		boolean isValid = customerTS.isValidVAT(168027852);
		
		// Then
		assertTrue("Number 168027852 is valid", isValid);
	}
	

	@Test
	public void isValidVATWithZeroCheckDigit() {
		// Given
		CustomerTransactionScripts customerTS = new CustomerTransactionScripts();
		
		// When, then
		assertTrue("Number 168027810 should have 0 check digit", 
				customerTS.isValidVAT(168027810));
	}
	
	@Test
	public void isValidVATNumberOutOfRange() {
		// Given
		CustomerTransactionScripts customerTS = new CustomerTransactionScripts();
		
		// When, then
		assertFalse("Number 100 not in the range 100000000 - 999999999", 
				customerTS.isValidVAT(100));
	}

	@Test
	public void isValidVATInvalidFirstDigit() {
		// Given
		CustomerTransactionScripts customerTS = new CustomerTransactionScripts();
				
		// When, then
		assertFalse("Number 400123120 starts with an invalid digit", 
				customerTS.isValidVAT(400123120));
	}	
	

}
