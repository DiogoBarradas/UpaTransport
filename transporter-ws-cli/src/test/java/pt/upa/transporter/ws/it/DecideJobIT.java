package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class DecideJobIT extends BaseTransporterIT {
	
	private JobView jobtest;
	private JobView jobtest2;
	private List<JobView> jobs;
	
	@Before
	public void setUp() {
		jobtest = null;
		jobtest2 = null;
		port.clearJobs();
		port2.clearJobs();
		jobs = new ArrayList<JobView>();
	}
	
	@After
	public void tearDown() {
		jobtest = null;
		jobtest2 = null;
		port.clearJobs();
		port2.clearJobs();
		jobs = new ArrayList<JobView>();
	}
	
	
	  @Test(expected=BadJobFault_Exception.class)
	    public void testDecideNoJobID() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
	    	
	    	jobtest = port.requestJob("Lisboa", "Beja", 51);
	    	JobView alterado = null;
	    	jobs.add(jobtest);
	    	
	    	jobtest2 = port2.requestJob("Lisboa", "Braga", 60);
	    	JobView alterado2 = null;
	    	jobs.add(jobtest2);

	    	for(JobView j: jobs){
	    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
	    			alterado = port.decideJob("UpaTransporter3:1", true);
	    		}
	    		if(j.getJobIdentifier().equals("UpaTransporter2:1")){
	    			alterado = port.decideJob("UpaTransporter3:1", true);
	    		}
	    	}

	        // if the assert fails, the test fails
	    }
	    
	    @Test
	    public void testDecideJobYesWithCompleted() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception, InterruptedException{
	    	
	    	int x = 1;
	    	
	    	jobtest2 = port2.requestJob("Lisboa", "Braga", 60);
	    	JobView alterado2 = null;
	    	jobs.add(jobtest2);
	    	
	    	jobtest = port.requestJob("Lisboa", "Beja", 51);
	    	JobView alterado = null;
	    	jobs.add(jobtest);
	    	    	
	    	for(JobView j: jobs){
	    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
	    			alterado = port.decideJob("UpaTransporter1:1", true);
	    		}
	    	}
	    	assertEquals(JobStateView.ACCEPTED,port.jobStatus(alterado.getJobIdentifier()).getJobState());
	    	System.out.println("A mudar para COMPLETED...");
	    	Thread.sleep(16000);
	    	assertEquals(JobStateView.COMPLETED,port.jobStatus(alterado.getJobIdentifier()).getJobState());

	        // if the assert fails, the test fails
	    }
 
	    @Test
	    public void testDecideJobNo() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
	    	
	    	jobtest2 = port2.requestJob("Lisboa", "Braga", 60);
	    	JobView alterado2 = null;
	    	jobs.add(jobtest2);
	    	
	    	jobtest = port.requestJob("Lisboa", "Beja", 51);
	    	JobView alterado = null;
	    	jobs.add(jobtest);
	    	    	
	    	for(JobView j: jobs){
	    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
	    			alterado = port.decideJob("UpaTransporter1:1", false);
	    		}
	    	}
	    	assertEquals(JobStateView.REJECTED,port.jobStatus(alterado.getJobIdentifier()).getJobState());

	        // if the assert fails, the test fails
	    }
	
}
