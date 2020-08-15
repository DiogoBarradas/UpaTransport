package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Unit Test suite
 *  The purpose of this class is to test TransporterPort locally.
 */
public class TransporterPortTest {

    // members
	
	private TransporterPort localport1;
	private TransporterPort localport2;
	private JobView jobtest;
	private JobView jobtest2;
	private List<JobView> jobs;
	

    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	localport1 = new TransporterPort("UpaTransporter1");
    	localport2 = new TransporterPort("UpaTransporter2");
    	jobtest = new JobView();
    	jobtest2 = new JobView();
    	jobs = new ArrayList<JobView>();
    }

    @After
    public void tearDown() {
    	localport1 = null;
    	localport2 = null;
    	jobtest = null;
    	jobtest2 = null;
    	jobs = null;
    }

    // tests

    @Test
    public void testPing(){

    	final String result1 = localport1.ping("UpaTransporter1");
    	final String result2 = localport2.ping("UpaTransporter2");
    		
    	assertEquals("UpaTransporter1", result1);
    	assertEquals("UpaTransporter2", result2);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobImparWithImparPrice()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result = localport1.requestJob("Lisboa", "Setúbal", 21);
    	
    	assertEquals("UpaTransporter1", result.getCompanyName());
    	assertEquals("Setúbal", result.getJobDestination());
    	assertEquals("UpaTransporter1:1", result.getJobIdentifier());
    	assertEquals("Lisboa", result.getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, result.getJobState());
    	assertTrue(result.getJobPrice() < 21);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobParWithParPrice()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result = localport2.requestJob("Lisboa", "Porto", 20);

    	assertEquals("UpaTransporter2", result.getCompanyName());
    	assertEquals("Porto", result.getJobDestination());
    	assertEquals("UpaTransporter2:1", result.getJobIdentifier());
    	assertEquals("Lisboa", result.getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, result.getJobState());
    	assertTrue(result.getJobPrice() < 20);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobImparWithParPrice()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result = localport1.requestJob("Lisboa", "Setúbal", 20);
    		
    	assertEquals("UpaTransporter1", result.getCompanyName());
    	assertEquals("Setúbal", result.getJobDestination());
    	assertEquals("UpaTransporter1:1", result.getJobIdentifier());
    	assertEquals("Lisboa", result.getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, result.getJobState());
    	assertTrue(result.getJobPrice() > 20);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobParWithImparPrice()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result = localport2.requestJob("Lisboa", "Porto", 21);

    	assertEquals("UpaTransporter2", result.getCompanyName());
    	assertEquals("Porto", result.getJobDestination());
    	assertEquals("UpaTransporter2:1", result.getJobIdentifier());
    	assertEquals("Lisboa", result.getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, result.getJobState());
    	assertTrue(result.getJobPrice() > 21);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobPriceOver100()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Setúbal", 120);
    	final JobView result2 = localport2.requestJob("Lisboa", "Porto", 120);
    		
    	assertEquals(null, result1);
    	assertEquals(null, result2);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobPriceBetween0And1()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Setúbal", 0);
    	final JobView result2 = localport1.requestJob("Lisboa", "Setúbal", 1);
    	final JobView result3 = localport2.requestJob("Lisboa", "Porto", 0);
    	final JobView result4 = localport2.requestJob("Lisboa", "Porto", 1);
    		
    	assertEquals(0, result1.getJobPrice());
    	assertEquals(0, result2.getJobPrice());
    	assertEquals(0, result3.getJobPrice());
    	assertEquals(0, result4.getJobPrice());
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobPriceBetween1And10()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Setúbal", 5);
    	final JobView result2 = localport2.requestJob("Lisboa", "Porto", 5);
    		
    	assertTrue(result1.getJobPrice() < 5);
    	assertTrue(result2.getJobPrice() < 5);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobPriceBetween10And100()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Setúbal", 50);
    	final JobView result2 = localport2.requestJob("Lisboa", "Porto", 50);
    	final JobView result3 = localport1.requestJob("Lisboa", "Setúbal", 51);
    	final JobView result4 = localport2.requestJob("Lisboa", "Porto", 51);
    		
    	assertTrue(result1.getJobPrice() > 50);
    	assertTrue(result2.getJobPrice() < 50);
    	assertTrue(result3.getJobPrice() < 51);
    	assertTrue(result4.getJobPrice() > 51);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRequestJobSameTransporter()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Faro", 50);
    	final JobView result2 = localport1.requestJob("Beja", "Leiria", 60);
    	final JobView result3 = localport1.requestJob("Coimbra", "Guarda", 25);
    	final JobView result4 = localport2.requestJob("Lisboa", "Porto", 90);
    	final JobView result5 = localport2.requestJob("Braga", "Leiria", 15);
    	final JobView result6 = localport2.requestJob("Viseu", "Vila Real", 78);
    	
    		
    	assertEquals("UpaTransporter1:1",result1.getJobIdentifier());
    	assertEquals("UpaTransporter1:2",result2.getJobIdentifier());
    	assertEquals("UpaTransporter1:3",result3.getJobIdentifier());
    	assertEquals("UpaTransporter2:1",result4.getJobIdentifier());
    	assertEquals("UpaTransporter2:2",result5.getJobIdentifier());
    	assertEquals("UpaTransporter2:3",result6.getJobIdentifier());
        // if the assert fails, the test fails
    }
    
    @Test(expected=BadPriceFault_Exception.class)
    public void testRequestNegativePrice()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Faro", -10);
    	final JobView result2 = localport2.requestJob("Lisboa", "Porto", -50);

        // if the assert fails, the test fails
    }
    
    @Test(expected=BadLocationFault_Exception.class)
    public void testRequestBadOrigin()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Porto", "Lisboa", 40);
    	final JobView result2 = localport2.requestJob("Lleiria", "Lisboa", 80);


        // if the assert fails, the test fails
    }
 
    @Test(expected=BadLocationFault_Exception.class)
    public void testRequestBadDestination()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	final JobView result1 = localport1.requestJob("Lisboa", "Porto", 40);
    	final JobView result2 = localport2.requestJob("Lisboa", "Lleiria", 80);


        // if the assert fails, the test fails
    }

    @Test(expected=BadJobFault_Exception.class)
    public void testDecideNoJobID() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobtest = localport1.requestJob("Lisboa", "Beja", 51);
    	JobView alterado = null;
    	jobs.add(jobtest);
    	
    	jobtest2 = localport2.requestJob("Lisboa", "Braga", 60);
    	JobView alterado2 = null;
    	jobs.add(jobtest2);

    	for(JobView j: jobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			alterado = localport1.decideJob("UpaTransporter3:1", true);
    		}
    	}

        // if the assert fails, the test fails
    }
    
    @Test
    public void testDecideJobYes() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobtest2 = localport2.requestJob("Lisboa", "Braga", 60);
    	JobView alterado2 = null;
    	jobs.add(jobtest2);
    	
    	jobtest = localport1.requestJob("Lisboa", "Beja", 51);
    	JobView alterado = null;
    	jobs.add(jobtest);
    	    	
    	for(JobView j: jobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			alterado = localport1.decideJob("UpaTransporter1:1", true);
    		}
    	}
    	assertEquals(JobStateView.ACCEPTED,alterado.getJobState());

        // if the assert fails, the test fails
    }
    
    @Test
    public void testDecideJobNo() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobtest2 = localport2.requestJob("Lisboa", "Braga", 60);
    	JobView alterado2 = null;
    	jobs.add(jobtest2);
    	
    	jobtest = localport1.requestJob("Lisboa", "Beja", 51);
    	JobView alterado = null;
    	jobs.add(jobtest);
    	    	
    	for(JobView j: jobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			alterado = localport1.decideJob("UpaTransporter1:1", false);
    		}
    	}
    	assertEquals(JobStateView.REJECTED,alterado.getJobState());

        // if the assert fails, the test fails
    }
    
    @Test
    public void testJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception{

    	final JobView result = localport1.requestJob("Lisboa", "Faro", 51);
    	
    	assertEquals("UpaTransporter1",localport1.jobStatus("UpaTransporter1:1").getCompanyName());
    	assertEquals("Faro", localport1.jobStatus("UpaTransporter1:1").getJobDestination());
    	assertEquals("UpaTransporter1:1", localport1.jobStatus("UpaTransporter1:1").getJobIdentifier());
    	assertEquals("Lisboa", localport1.jobStatus("UpaTransporter1:1").getJobOrigin());
    	assertEquals(JobStateView.PROPOSED, localport1.jobStatus("UpaTransporter1:1").getJobState());
    	assertTrue(localport1.jobStatus("UpaTransporter1:1").getJobPrice() < 51);
    		
        // if the assert fails, the test fails
    }
    
    @Test
    public void testJobStatusNull() throws BadLocationFault_Exception, BadPriceFault_Exception{

    	final JobView result = localport1.requestJob("Lisboa", "Faro", 51);

    	jobtest.setJobIdentifier("UpaTransporter1:1");
    	
    	assertEquals(null ,localport1.jobStatus("UpaTransporter2:1"));
    		
        // if the assert fails, the test fails
    }
    
    
    @Test
    public void testListJobs() throws BadLocationFault_Exception, BadPriceFault_Exception{

    	final JobView result1 = localport1.requestJob("Lisboa", "Faro", 51);
    	jobs.add(result1);   	
    	final JobView result2 = localport1.requestJob("Lisboa", "Leiria", 81);
    	jobs.add(result2);   	
    	final JobView result3 = localport1.requestJob("Beja", "Faro", 97);
    	jobs.add(result3);

    	assertEquals(jobs, localport1.listJobs());

        // if the assert fails, the test fails
    }
    

    @Test
    public void testClearJobs() throws BadLocationFault_Exception, BadPriceFault_Exception{

    	final JobView result1 = localport1.requestJob("Lisboa", "Faro", 51); 	
    	final JobView result2 = localport1.requestJob("Lisboa", "Leiria", 81);
    	final JobView result3 = localport1.requestJob("Beja", "Faro", 97);
    	
    	localport1.clearJobs();
    	
    	assertEquals(jobs, localport1.listJobs());

        // if the assert fails, the test fails
    }       
}