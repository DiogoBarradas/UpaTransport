package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class ClearJobsIT extends BaseTransporterIT {
	
	@Before
	public void setUp() {
		port.clearJobs();
		port2.clearJobs();
	}
	
	@Test
    public void testClearJobs() throws BadLocationFault_Exception, BadPriceFault_Exception{

		final List<JobView> jobs = new ArrayList<JobView>();
			
		final JobView result1 = port.requestJob("Lisboa", "Faro", 51);
    	jobs.add(result1);   	
    	final JobView result2 = port.requestJob("Lisboa", "Leiria", 81);
    	jobs.add(result2);   	
    	final JobView result3 = port2.requestJob("Braga", "Lisboa", 90);
    	jobs.add(result3);
    	
    	jobs.clear();
    	
    	port.clearJobs();
    	port2.clearJobs();
    	
    	assertEquals(jobs, port.listJobs());
    	assertEquals(jobs, port2.listJobs());

        // if the assert fails, the test fails
    }       
}
