package pt.upa.transporter.ws.it;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

/**
 * Super class for integration test suites
 * 
 * Loads test properties from configuration file
 */
public abstract class BaseTransporterIT {

	protected static TransporterPortType port = null;
	protected static TransporterPortType port2 = null;

	@BeforeClass					
	public static void oneTimeSetup() throws Exception {

		String uddiURL = "http://localhost:9090/";
		String wsName = "UpaTransporter1";
		String wsName2 = "UpaTransporter2";
		
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", wsName);
		String endpointAddress = uddiNaming.lookup(wsName);
		
		TransporterService service = new TransporterService();
		port = service.getTransporterPort();
		
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		System.out.printf("Looking for '%s'%n", wsName2);
		String endpointAddress2 = uddiNaming.lookup(wsName2);
		
		TransporterService service2 = new TransporterService();
		port2 = service2.getTransporterPort();
		
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider2 = (BindingProvider) port2;
		Map<String, Object> requestContext2 = bindingProvider2.getRequestContext();
		requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);
		
	}

	@AfterClass
	public static void cleanup() {
		port = null;
		port2 = null;
	}

}
