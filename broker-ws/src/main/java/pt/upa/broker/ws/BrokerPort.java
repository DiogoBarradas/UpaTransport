package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebService;

import pt.upa.broker.BrokerApplication;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.2_0.wsdl",
	    name="UpaBroker",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType {

	Map<String,TransporterPortType> transportersports = new HashMap<String,TransporterPortType>() ;
	BrokerPortType port2;

	Map<String,String> zonas = new HashMap<String,String>() ;
	{
		zonas.put("Lisboa","Centro");
		zonas.put("Leiria","Centro");
		zonas.put("Santarém","Centro");
		zonas.put("Castelo Branco","Centro");
		zonas.put("Aveiro","Centro");
		zonas.put("Coimbra","Centro");
		zonas.put("Viseu","Centro");
		zonas.put("Guarda","Centro");
		zonas.put("Setúbal", "Sul");
		zonas.put("Évora", "Sul");
		zonas.put("Portalegre", "Sul");
		zonas.put("Beja", "Sul");
		zonas.put("Faro", "Sul");
		zonas.put("Porto","Norte");
		zonas.put("Braga","Norte");
		zonas.put("Viana do Castelo","Norte");
		zonas.put("Vila Real","Norte");
		zonas.put("Bragança","Norte");
	}
	
	//Ĩnicializa uma lista de estados das transportadoras
	List<TransportView> estadosTransporter = new ArrayList<>(); 

	//importa lista de transportadoras
	
	JobView jobaccepted = null;
	
	TransporterPortType port = null;
	
	public BrokerPort(Map<String,TransporterPortType> transportersports){
		this.transportersports = transportersports;
	}
	
	public BrokerPort(Map<String,TransporterPortType> transportersports,BrokerPortType port2){
		this.transportersports = transportersports;
		this.port2 = port2;
		
	}
	
	
	
	@Override
	public String ping(String name) {
		
		if(name.equals("kill")){
			System.exit(0);
		}
		
		for(Entry<String, TransporterPortType> entry : transportersports.entrySet()) {
			
			TransporterPortType port = entry.getValue();
			
			port.ping(name);
		}
		
		System.out.println("Está vivo");
		
		return "A fazer ping" ;
		
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		String returnID = null;
		
			try{
				
				//Ĩnicializa uma lista de jobs das transportadoras
				List<JobView> jobsTransporter = new ArrayList<>();
				

				if(zonas.containsKey(origin) == false){
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(origin);
					throw new UnknownLocationFault_Exception("Origem desconhecida: ", faultInfo);
				} 
				if(zonas.containsKey(destination) == false){
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Destino desconhecido: ", faultInfo);
				} 
				if(price < 0){
					InvalidPriceFault faultInfo = new InvalidPriceFault();
					faultInfo.setPrice(price);
					throw new InvalidPriceFault_Exception("Preço inválido: ", faultInfo);
				}
				
				TransportView transportadora = new TransportView();
				transportadora.setDestination(destination);
				transportadora.setOrigin(origin);
				transportadora.setState(TransportStateView.REQUESTED);
				estadosTransporter.add(transportadora);
				
				//Redirecciona a função requestJob() em cada transportadora
				for(Entry<String, TransporterPortType> entry : transportersports.entrySet()) {
					
					TransporterPortType port = entry.getValue();
		    	
				    JobView currentjob = port.requestJob(origin, destination, price);

					if(currentjob != null){
					    transportadora.setState(TransportStateView.BUDGETED);
						jobsTransporter.add(currentjob);
					} 
				}
				
				if(jobsTransporter.isEmpty()){
					UnavailableTransportFault faultInfo = new UnavailableTransportFault();
					faultInfo.setOrigin(origin);
					faultInfo.setDestination(destination);
					throw new UnavailableTransportFault_Exception("Nenhuma transportadora disponivel ", faultInfo);
				}
				
				//Ciclo for each para encontrar melhor proposta
				int precoMin = 120;
					
				for(JobView j :jobsTransporter){

					int preco = j.getJobPrice();
					if(preco < precoMin){
							precoMin = preco;
							jobaccepted = j;
					} 
				}
				
				for(JobView j :jobsTransporter){
					String company = j.getCompanyName();
					port = transportersports.get(company);
					System.out.println(j.getJobPrice());
					if(j == jobaccepted){
						if(j.getJobPrice() < price || j.getJobPrice() == 0){
							port.decideJob(j.getJobIdentifier(), true);
							transportadora.setTransporterCompany(j.getCompanyName());
							transportadora.setId(j.getJobIdentifier());
							transportadora.setPrice(j.getJobPrice());
							returnID = transportadora.getId();
							transportadora.setState(TransportStateView.BOOKED);
							if(port2 != null){
								port2.update(transportadora);
							}
						} else {
							port.decideJob(j.getJobIdentifier(), false);
							transportadora.setState(TransportStateView.FAILED);
							UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
							faultInfo.setBestPriceFound(precoMin);
							throw new UnavailableTransportPriceFault_Exception("A viagem mais barata encontrada tinha o seguinte preco ", faultInfo);
						}
					} else{
						port.decideJob(j.getJobIdentifier(), false);
						transportadora.setState(TransportStateView.FAILED);
					}
					
				}
				
			} catch (BadLocationFault_Exception e){
				UnknownLocationFault faultInfo = new UnknownLocationFault();
				faultInfo.setLocation(e.getFaultInfo().getLocation());
				throw new UnknownLocationFault_Exception("Cidade desconhecida: ", faultInfo);
				
			} catch (BadPriceFault_Exception e){
				InvalidPriceFault faultInfo = new InvalidPriceFault();
				faultInfo.setPrice(e.getFaultInfo().getPrice());
				throw new InvalidPriceFault_Exception("Preço inválido: ", faultInfo);
			} catch (BadJobFault_Exception e) {
				System.out.println("Erro aqui");
			}
			
			return returnID;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
				
		System.out.println(id);

		jobaccepted = port.jobStatus(id);


		
		for(TransportView t: estadosTransporter){
			if(t.getId().equals(id)){
				System.out.println(id);
				if(t.getState() == ( TransportStateView.BOOKED)){
					if(jobaccepted.getJobState() == (JobStateView.HEADING)){
						t.setState(TransportStateView.HEADING);
						return t;
					}
					if(jobaccepted.getJobState() == (JobStateView.ONGOING)){
						t.setState(TransportStateView.ONGOING);
						return t;
					}
					if(jobaccepted.getJobState() == (JobStateView.COMPLETED)){
						t.setState(TransportStateView.COMPLETED);
						return t;
					}
				}
				if(t.getState() != (TransportStateView.BOOKED)){
					return t;
				}	
			}
		}
		
		UnknownTransportFault faultInfo = new UnknownTransportFault();
		faultInfo.setId(id);
		throw new UnknownTransportFault_Exception("A transportadora com este id não existe: ", faultInfo);

	}

	@Override
	public List<TransportView> listTransports() {
		System.out.println(estadosTransporter.size());
		return estadosTransporter;
	}

	@Override
	public void clearTransports() {
		estadosTransporter.clear();

		for(Entry<String, TransporterPortType> entry : transportersports.entrySet()) {
			
			TransporterPortType port = entry.getValue();
			
			port.clearJobs();
			
			
		}
		
		if(port2 != null){
			port2.clearTransports();
		}
		
	}

	@Override
	public String pingBroker(String name) {
		
		System.out.println("Estás vivo? " + name);
		BrokerApplication.x = "Estás vivo? ";
		return "Sim";
	}
	
	@Override
	public void update(TransportView a){
		estadosTransporter.add(a);
	}


	

}
