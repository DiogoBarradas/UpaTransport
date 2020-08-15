package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class ListTransportsIT extends BaseBrokerIT{

	 @Before
	    public void setUp() {
	    	port.clearTransports();
	    }

	    @After
	    public void tearDown() {
	    	port.clearTransports();
	    }
		
	
	@Test
	 public void listTransports() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    
			List<TransportView> transports = new ArrayList<TransportView>();
			
			port.requestTransport("Lisboa", "Beja", 51);
		 	
		 	assertEquals("Beja", port.listTransports().get(0).getDestination());
		 	assertEquals("UpaTransporter1:1", port.listTransports().get(0).getId());
		 	assertEquals("Lisboa", port.listTransports().get(0).getOrigin());
		 	assertTrue(port.listTransports().get(0).getPrice() < 51);
		 	assertEquals("UpaTransporter1", port.listTransports().get(0).getTransporterCompany());
		 	
		 	transports.clear();
		 	
	    	
	 }
	
}
