package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;


public class RequestTransportIT extends BaseBrokerIT {

    @Before
    public void setUp() {
    	port.clearTransports();
    }

    @After
    public void tearDown() {
    	port.clearTransports();
    }
	
	
	@Test
	 public void requestTransportImpar()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Setúbal", 21);
	    	
	    	assertEquals("UpaTransporter1:1", retorno);
	    	
	 }
 
	@Test
	 public void requestTransportPar()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Porto", 20);
	    	
	    	assertEquals("UpaTransporter2:1", retorno);
	    	
	 }
	
	@Test(expected=InvalidPriceFault_Exception.class)
	 public void requestTransportWithPriceUnder0()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Porto", -5);

	    	
	 }
	
	@Test(expected=UnknownLocationFault_Exception.class)
	 public void requestTransportBadOrigin()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Madrid", "Porto", 60);

	    	
	 }
	
	@Test(expected=UnknownLocationFault_Exception.class)
	 public void requestTransportBadDestination()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Porto", "Londres", 60);

	    	
	 }
	
	@Test(expected=UnavailableTransportFault_Exception.class)
	 public void requestTransportNorthToSouth()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Porto", "Faro", 60);

	    	
	 }
	

	
	@Test(expected=UnavailableTransportPriceFault_Exception.class)
	 public void requestTransportNoOffers()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Faro", 60);

	 }

	@Test
	 public void requestTransportPrice0()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Porto", 0);
		 	
		 	assertEquals("UpaTransporter2:1", retorno);

	    	
	 }
	
	@Test
	 public void requestTransportPrice1()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Beja", 1);
		 	
		 	assertEquals("UpaTransporter1:1", retorno);

	    	
	 }
		
	@Test(expected=UnavailableTransportFault_Exception.class)
	 public void requestTransportPriceOver100()
			 throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
				UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
	    	
		 	String retorno = port.requestTransport("Lisboa", "Faro", 120);

	 }
	
}
