package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.net.ConnectException;
import java.util.Map;
import java.util.Scanner;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;
import pt.upa.broker.ws.cli.BrokerClient;

public class BrokerClientApplication {

public static void main(String[] args) throws Exception {
	System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...");
	// Check arguments
	if (args.length < 2) {
		System.err.println("Argument(s) missing!");
		System.err.printf("Usage: java %s uddiURL name%n", BrokerClient.class.getName());
		return;
	}

	String uddiURL = args[0];
	String name = args[1];

	System.out.printf("Contacting UDDI at %s%n", uddiURL);
	UDDINaming uddiNaming = new UDDINaming(uddiURL);

	System.out.printf("Looking for '%s'%n", name);
	String endpointAddress = uddiNaming.lookup(name);

	if (endpointAddress == null) {
		System.out.println("Not found!");
		return;
	} else {
		System.out.printf("Found %s%n", endpointAddress);
	}

	System.out.println("Creating stub ...");
	BrokerService service = new BrokerService();
	BrokerPortType port = service.getBrokerPort();

	System.out.println("Setting endpoint address ...");
	BindingProvider bindingProvider = (BindingProvider) port;
	Map<String, Object> requestContext = bindingProvider.getRequestContext();
	requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	
	String novo = endpointAddress;
	
	while(true){
	
		Scanner scanner = new Scanner(System.in);
		
		Scanner scanner2 = new Scanner(System.in); 
		
		Scanner scanner3 = new Scanner(System.in); 
		
		System.out.println("Escolha uma opçao");
		
		String cmd = scanner.nextLine();
		
		
		if(cmd.equals("1") || cmd.equals("2") || cmd.equals("3") || cmd.equals("4") || cmd.equals("5") || cmd.equals("6")){
		
			
			try{
			switch(cmd){
			
				case "1":
					
						String x = port.ping("teste");
						System.out.println(x);
						break;
				
				
				case "2":
					try{
						System.out.print("Introduza uma origem: ");
						String origin = scanner2.nextLine();
						System.out.print("Introduza um destino: ");
						String destination = scanner2.nextLine();
						System.out.print("Introduza um preço: ");
						int price = scanner.nextInt();
						
						System.out.println(port.requestTransport(origin, destination, price));
	
						break;
					} catch(UnknownLocationFault_Exception e){
						System.out.println(e.getMessage() + e.getFaultInfo().getLocation());
						continue;
					} catch(InvalidPriceFault_Exception e){
						System.out.println(e.getMessage() + e.getFaultInfo().getPrice());
						continue;
					} catch(UnavailableTransportPriceFault_Exception e){
						System.out.println(e.getMessage() + e.getFaultInfo().getBestPriceFound());
						continue;
					} catch(UnavailableTransportFault_Exception e){
						System.out.println(e.getMessage() + e.getFaultInfo().getDestination());
						continue;
					}
					
				case "3":
					try{
						System.out.print("Introduza o ID da sua viagem: ");
						String ID = scanner3.nextLine();
						String value = port.viewTransport(ID).getState().value();    
						System.out.println("Transportadora: " + port.viewTransport(ID).getTransporterCompany());
						System.out.println("Origem: " + port.viewTransport(ID).getOrigin());
						System.out.println("Destino: " + port.viewTransport(ID).getDestination());
						System.out.println("Preço: " + port.viewTransport(ID).getPrice());
						System.out.println("Estado: " + value);
						break;
					} catch(UnknownTransportFault_Exception e){
						System.out.println(e.getMessage() + e.getFaultInfo().getId());
						continue;
					}
					
				case "4":

					for(TransportView transporter: port.listTransports()){
						System.out.println(transporter.getId());
					}
					break;
					
					
				case "5":
				
					port.clearTransports();
					System.out.println("Lista de viagens apagada!");
					break;
					
				case "6":
					
					String y = port.ping("kill");
					System.out.println(y);
					break;
					
					
			}
			}catch(Exception e){
				
				System.out.println("Reconnecting to server...");
				 uddiNaming = new UDDINaming(uddiURL);

				
				 endpointAddress = uddiNaming.lookup(name);

				if (endpointAddress == null) {
					
					return;
				} else {
					
				}

				
				 service = new BrokerService();
				port = service.getBrokerPort();

				
				 bindingProvider = (BindingProvider) port;
				 requestContext = bindingProvider.getRequestContext();
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
					 
				
			}
		
		}else{
			System.out.println("Operação inválida!");
		}
		
	}
}
}