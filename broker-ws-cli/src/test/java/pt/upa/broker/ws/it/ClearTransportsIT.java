package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class ClearTransportsIT extends BaseBrokerIT{

	
	@Before
    public void setUp() {
    	port.clearTransports();
    }

    @After
    public void tearDown() {
    	
    }
	

@Test
 public void clearTransports() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
   
		
		port.requestTransport("Lisboa", "Beja", 51);
		port.clearTransports();
	 	
	 	assertTrue(port.listTransports().isEmpty());
    	
 }
}
