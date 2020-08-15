package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class RequestJobIT extends BaseTransporterIT{
	
	private JobView result;
	private JobView result2;
	
	@Before
	public void tearDown() {
		result = null;
		result2 = null;
		port.clearJobs();
		port2.clearJobs();
	}
	
	 @Test
	 public void testRequestJobImparWithImparPrice()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
		 	result = port.requestJob("Lisboa", "Setúbal", 21);
	    	
	    	assertEquals("UpaTransporter1", result.getCompanyName());
	    	assertEquals("Setúbal", result.getJobDestination());
	    	assertEquals("UpaTransporter1:1", result.getJobIdentifier());
	    	assertEquals("Lisboa", result.getJobOrigin());
	    	assertEquals(JobStateView.PROPOSED, result.getJobState());
	    	assertTrue(result.getJobPrice() < 21);
	    	
	    	result2 = port2.requestJob("Lisboa", "Setúbal", 21);
    		
	    	assertEquals(null, result2);
	    	
	    }
	    
	    @Test
	    public void testRequestJobParWithParPrice()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Porto", 20);
	    		
	    	assertEquals(null, result);
	    	
	    	result2 = port2.requestJob("Lisboa", "Porto", 20);
	    	assertEquals("UpaTransporter2", result2.getCompanyName());
	    	assertEquals("Porto", result2.getJobDestination());
	    	assertEquals("UpaTransporter2:1", result2.getJobIdentifier());
	    	assertEquals("Lisboa", result2.getJobOrigin());
	    	assertEquals(JobStateView.PROPOSED, result2.getJobState());
	    	assertTrue(result2.getJobPrice() < 20);
	    }
	  
	   
	    @Test
	    public void testRequestJobImparWithParPrice()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 20);
	
	    	assertEquals("UpaTransporter1", result.getCompanyName());
	    	assertEquals("Setúbal", result.getJobDestination());
	    	assertEquals("UpaTransporter1:1", result.getJobIdentifier());
	    	assertEquals("Lisboa", result.getJobOrigin());
	    	assertEquals(JobStateView.PROPOSED, result.getJobState());
	    	assertTrue(result.getJobPrice() > 20);
	    	
	    	result2 = port2.requestJob("Lisboa", "Setúbal", 20);
    		
	    	assertEquals(null, result2);
	    }
	    
	    @Test
	    public void testRequestJobParWithImparPrice()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Porto", 21);
	    		
	    	assertEquals(null, result);
	    	
	    	result2 = port2.requestJob("Lisboa", "Porto", 21);
	    	assertEquals("UpaTransporter2", result2.getCompanyName());
	    	assertEquals("Porto", result2.getJobDestination());
	    	assertEquals("UpaTransporter2:1", result2.getJobIdentifier());
	    	assertEquals("Lisboa", result2.getJobOrigin());
	    	assertEquals(JobStateView.PROPOSED, result2.getJobState());
	    	assertTrue(result2.getJobPrice() > 21);
	    }
	    
	    
	    @Test
	    public void testRequestJobPriceOver100()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 120);
	    	result2 = port2.requestJob("Lisboa", "Porto", 120);
	    		
	    	assertEquals(null, result);
	    	assertEquals(null, result2);

	    }
	    
	    @Test
	    public void testRequestJobPriceBetween0And1()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 0);
	    	assertEquals(0, result.getJobPrice());
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 1);
	    	assertEquals(0, result.getJobPrice());
	    	
	    	result2 = port2.requestJob("Lisboa", "Porto", 0);
	    	assertEquals(0, result2.getJobPrice());
	    	
	    	result2 = port2.requestJob("Lisboa", "Porto", 1);
	    	assertEquals(0, result2.getJobPrice());
	    }

	    @Test
	    public void testRequestJobPriceBetween1And10()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 5);
	    	result2 = port2.requestJob("Lisboa", "Porto", 5);
	    		
	    	assertTrue(result.getJobPrice() < 5);
	    	assertTrue(result2.getJobPrice() < 5);
	    }
	    
	    @Test
	    public void testRequestJobPriceBetween10And100()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Setúbal", 50);
	    	assertTrue(result.getJobPrice() > 50);
	    	result = port.requestJob("Lisboa", "Porto", 50);
	    	assertEquals(null, result);
	    	result = port.requestJob("Lisboa", "Setúbal", 51);
	    	assertTrue(result.getJobPrice() < 51);
	    	result = port.requestJob("Lisboa", "Porto", 51);
	    	assertEquals(null, result);
	    	
	    	
	     	result2 = port2.requestJob("Lisboa", "Porto", 50);
	    	assertTrue(result2.getJobPrice() < 50);
	    	result2 = port2.requestJob("Lisboa", "Setúbal", 50);
	    	assertEquals(null, result2);
	    	result2 = port2.requestJob("Lisboa", "Porto", 51);
	    	assertTrue(result2.getJobPrice() > 51);
	    	result2 = port2.requestJob("Lisboa", "Setúbal", 51);
	    	assertEquals(null, result2);
	    }
	    
	    @Test
	    public void testRequestJobSameTransporter()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Faro", 51);
	    	assertEquals("UpaTransporter1:1",result.getJobIdentifier());
	    	result = port.requestJob("Lisboa", "Faro", 51);
	    	assertEquals("UpaTransporter1:2",result.getJobIdentifier());
	    	result = port.requestJob("Lisboa", "Faro", 51);
	    	assertEquals("UpaTransporter1:3",result.getJobIdentifier());
	    	
	    	result2 = port2.requestJob("Lisboa", "Porto", 51);
	    	assertEquals("UpaTransporter2:1",result2.getJobIdentifier());
	    	result2 = port2.requestJob("Lisboa", "Porto", 51);
	    	assertEquals("UpaTransporter2:2",result2.getJobIdentifier());
	    	result2 = port2.requestJob("Lisboa", "Porto", 51);
	    	assertEquals("UpaTransporter2:3",result2.getJobIdentifier());
	    }
	    
	    @Test(expected=BadPriceFault_Exception.class)
	    public void testRequestNegativePrice()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Faro", -10);
	    	result2 = port2.requestJob("Lisboa", "Porto", -10);
	    }
	    
	    @Test(expected=BadLocationFault_Exception.class)
	    public void testRequestBadOrigin()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lleiria", "Lisboa", 41);
	    	result2 = port2.requestJob("Lleiria", "Lisboa", 41);
	    }
	 
	    @Test(expected=BadLocationFault_Exception.class)
	    public void testRequestBadDestination()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
	    	
	    	result = port.requestJob("Lisboa", "Pporto", 41);
	    	result2 = port2.requestJob("Lisboa", "Pporto", 41);

	    }
}
