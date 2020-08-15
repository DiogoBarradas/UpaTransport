package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class ViewTransportIT extends BaseBrokerIT {

	@Before
    public void setUp() {
		port.clearTransports();
    }

    @After
    public void tearDown() {
    	port.clearTransports();
    }
	
	
	@Test(expected=UnknownTransportFault_Exception.class)
	 public void viewTransportNoExist() throws UnknownTransportFault_Exception, InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	port.requestTransport("Lisboa", "Setúbal", 21);
		 	port.requestTransport("Lisboa", "Braga", 20);
		 	
		 	port.viewTransport("UpaTransporter3:1");

	 }
	
	@Test
	public void testTransportBooked() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		List<TransportView> Transports = new ArrayList<>();
		port.requestTransport("Lisboa", "Faro", 61);
		Transports = port.listTransports();
		TransportView x = (Transports.get(0));
		
		
		assertEquals(TransportStateView.BOOKED,x.getState());

	}
	
	
	
}
