package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class JobStatusIT extends BaseTransporterIT{

	@Before
	public void setUp() {
		port.clearJobs();
		port2.clearJobs();
	}
	
	@Test
    public void testJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception{

		port.requestJob("Lisboa", "Faro", 51);
    	
    	assertEquals("UpaTransporter1",port.jobStatus("UpaTransporter1:1").getCompanyName());
    	assertEquals("Faro", port.jobStatus("UpaTransporter1:1").getJobDestination());
    	assertEquals("UpaTransporter1:1", port.jobStatus("UpaTransporter1:1").getJobIdentifier());
    	assertEquals("Lisboa", port.jobStatus("UpaTransporter1:1").getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, port.jobStatus("UpaTransporter1:1").getJobState());
    	assertTrue(port.jobStatus("UpaTransporter1:1").getJobPrice() < 51);
    	
    	port2.requestJob("Lisboa", "Porto", 50);
    	
    	assertEquals("UpaTransporter2",port2.jobStatus("UpaTransporter2:1").getCompanyName());
    	assertEquals("Porto", port2.jobStatus("UpaTransporter2:1").getJobDestination());
    	assertEquals("UpaTransporter2:1", port2.jobStatus("UpaTransporter2:1").getJobIdentifier());
    	assertEquals("Lisboa", port2.jobStatus("UpaTransporter2:1").getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, port2.jobStatus("UpaTransporter2:1").getJobState());
    	assertTrue(port2.jobStatus("UpaTransporter2:1").getJobPrice() < 50);
    	
    	
    	
    	
    		
        // if the assert fails, the test fails
    }
    
    @Test
    public void testJobStatusNull() throws BadLocationFault_Exception, BadPriceFault_Exception{

    	port.requestJob("Lisboa", "Faro", 51);
   	
    	assertEquals(null ,port.jobStatus("UpaTransporter2:1"));
    	
    	port2.requestJob("Lisboa", "Braga", 50);
       	
    	assertEquals(null ,port2.jobStatus("UpaTransporter3:1"));
    		
        // if the assert fails, the test fails
    }
    	
	
}
