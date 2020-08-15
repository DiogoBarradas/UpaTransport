package pt.upa.broker;


import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceException;

import example.ws.handler.HeaderHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPort;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.TransportView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;


public class BrokerApplication  {
	
	public static final String TOKEN = "UpaBroker1";
	public static final String CLASS_NAME = BrokerApplication.class.getSimpleName();

	public static String x = "";
	
	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", BrokerApplication.class.getName());
			return;
		}
		
		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		
		
		Map<String,String> transportersendpoints = new HashMap<String,String>() ;
		Map<String,TransporterPortType> transportersports = new HashMap<String,TransporterPortType>() ;
		
		try {

			uddiNaming = new UDDINaming(uddiURL);
				
			if(name.equals("UpaBroker1")){
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
				//UDDINaming uddiNaming = new UDDINaming(uddiURL);

				System.out.printf("Looking for '%s'%n", "UpaBroker2");
				String endpointAddress = uddiNaming.lookup("UpaBroker2");
				System.out.println();
				
				if (endpointAddress == null) {
					System.out.println("Not found!");
				} else {
					System.out.printf("Found %s%n", endpointAddress);
					transportersendpoints.put("UpaBroker2",endpointAddress);
					
					//get transporter port
					BrokerService service2 = new BrokerService();
					BrokerPortType port2 = service2.getBrokerPort();
					
					System.out.println("Setting endpoint address ...");
					BindingProvider bindingProvider = (BindingProvider) port2;
					Map<String, Object> requestContext = bindingProvider.getRequestContext();
					requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
					
					
					BrokerPort broker = new BrokerPort(transportersports,port2);
					endpoint = Endpoint.create(broker);

					// publish endpoint
					System.out.printf("Starting %s%n", url);
					endpoint.publish(url);

					// publish to UDDI
					System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
					uddiNaming = new UDDINaming(uddiURL);
					uddiNaming.rebind(name, url);
					
					for(int i=1;i<10;i++){
						System.out.printf("Contacting UDDI at %s%n", uddiURL);
						//UDDINaming uddiNaming = new UDDINaming(uddiURL);

						System.out.printf("Looking for '%s'%n", "UpaTransporter"+i );
						String endpointAddress2 = uddiNaming.lookup("UpaTransporter"+i);
						System.out.println();
						
						if (endpointAddress2 == null) {
							System.out.println("Not found!");
						} else {
							System.out.printf("Found %s%n", endpointAddress2);
							transportersendpoints.put("UpaTransporter"+i,endpointAddress2);
							
							//get transporter port
							TransporterService service = new TransporterService();
							TransporterPortType port = service.getTransporterPort();
							
							System.out.println("Setting endpoint address ...");
							BindingProvider bindingProvider2 = (BindingProvider) port;
							Map<String, Object> requestContext2 = bindingProvider2.getRequestContext();
							
							String initialValue = TOKEN;
							System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
							requestContext2.put(HeaderHandler.CONTEXT_PROPERTY, initialValue);
							
							requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);
							
							transportersports.put("UpaTransporter"+i,port);
							

							
							
							
							
							
						}
					}
					
					class Timer extends Thread{
						public void run(){
							while(true){
								port2.pingBroker("");
								
								try{
									Thread.sleep(5000);
								} catch(InterruptedException e){
									Thread.currentThread().interrupt();
									return;
								}
							}
						}
					}
					
					Timer t1 = new Timer();
					
					t1.run();
					
				}
				
				
				
			} else{
				BrokerPort broker = new BrokerPort(transportersports);
				endpoint = Endpoint.create(broker);

				// publish endpoint
				System.out.printf("Starting %s%n", url);
				endpoint.publish(url);

				// publish to UDDI
				System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(name, url);
				
				

				class TimerTimeout extends Thread{
					public void run(){
						
						while(true){
							try{
								Thread.sleep(10000);
								System.out.println(x);
								if(x == "Estás vivo? "){
									x = "";
								}else{
									
									throw new InterruptedException();
								}
							} catch(InterruptedException e){
								
								Thread.currentThread().interrupt();
								return;
							}
						}
					}
				}
				
				TimerTimeout t2 = new TimerTimeout();
				
				t2.run();
				System.out.println("Changing to UpaBroker1");
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind("UpaBroker1", url);
				
				for(int i=1;i<10;i++){
					System.out.printf("Contacting UDDI at %s%n", uddiURL);
					//UDDINaming uddiNaming = new UDDINaming(uddiURL);

					System.out.printf("Looking for '%s'%n", "UpaTransporter"+i );
					String endpointAddress = uddiNaming.lookup("UpaTransporter"+i);
					System.out.println();
					
					if (endpointAddress == null) {
						System.out.println("Not found!");
					} else {
						System.out.printf("Found %s%n", endpointAddress);
						transportersendpoints.put("UpaTransporter"+i,endpointAddress);
						
						//get transporter port
						TransporterService service = new TransporterService();
						TransporterPortType port = service.getTransporterPort();
						
						System.out.println("Setting endpoint address ...");
						BindingProvider bindingProvider = (BindingProvider) port;
						Map<String, Object> requestContext = bindingProvider.getRequestContext();
						
						String initialValue = TOKEN;
						System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
						requestContext.put(HeaderHandler.CONTEXT_PROPERTY, initialValue);
						
						requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
						
						transportersports.put("UpaTransporter"+i,port);
						
						//Adiciona um TransportView à lista de estados de transportadoras
						TransportView estado = new TransportView();
						estado.setId("UpaTransporter"+i);
						broker.listTransports().add(estado);
						
						
						
					}
				}
				
			}
			
			
			
				// wait
				System.out.println("Awaiting connections");
				System.out.println("Press enter to shutdown");
				System.in.read();

			

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind("UpaBroker1");
					System.out.printf("Deleted '%s' from UDDI%n", "UpaBroker1");
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

	}

}
