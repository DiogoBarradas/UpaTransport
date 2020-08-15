package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class connectionIT extends BaseBrokerIT {

	@Before
    public void setUp() {
		port.clearTransports();
    }

    @After
    public void tearDown() {
    	port.clearTransports();
    }
	
	
	@Test
	 public void checkConnection() throws UnknownTransportFault_Exception, InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, InterruptedException{
	    	
		port.requestTransport("Lisboa", "Beja", 51);
		List<TransportView> x = port.listTransports();
		port.ping("kill");
		Thread.currentThread().sleep(12000);
		port.requestTransport("Lisboa", "Beja", 51);
		List<TransportView> y = port.listTransports();
		
		assertTrue(x.equals(y));
		
	 }	
}
