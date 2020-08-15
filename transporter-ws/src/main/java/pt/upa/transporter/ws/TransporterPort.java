package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import example.ws.handler.HeaderHandler;

import javax.annotation.Resource;
import javax.jws.HandlerChain;

@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="UpaTransporter",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
)
@HandlerChain(file = "/hello_handler-chain.xml")


public class TransporterPort implements TransporterPortType {
	
	public static final String CLASS_NAME = TransporterPort.class.getSimpleName();
	
	@Resource
	private WebServiceContext webServiceContext;

	private Timer timer1;
	private Timer timer2;
	private Timer timer3;
	
	class ChangeState extends TimerTask{
	    
		Timer timer;
		JobView job;

		public ChangeState(Timer timer,JobView job){
			this.timer = timer;
			this.job = job;
		}

	    public void run(){
	    	 if ((job.getJobState()) == (JobStateView.ACCEPTED)){
                 job.setJobState(JobStateView.HEADING);
                 System.out.println("Estado mudado para HEADING");
                 timer.cancel(); //Terminate the timer thread
                 return;
             }
                
             if ((job.getJobState()) == (JobStateView.HEADING)){
                 job.setJobState(JobStateView.ONGOING);
                 System.out.println("Estado mudado para ONGOING");
                 timer.cancel(); //Terminate the timer thread
                 return;

             }
                    
             if ((job.getJobState()) == (JobStateView.ONGOING)){
                 job.setJobState(JobStateView.COMPLETED);
                 System.out.println("Estado mudado para COMPLETED");
                 timer.cancel(); //Terminate the timer thread
                 return;

             }
	    }
	}
	
	Map<String,String> zonaspar = new HashMap<String,String>();
			
	{
		zonaspar.put("Lisboa","Centro");
		zonaspar.put("Leiria","Centro");
		zonaspar.put("Santarém","Centro");
		zonaspar.put("Castelo Branco","Centro");
		zonaspar.put("Aveiro","Centro");
		zonaspar.put("Coimbra","Centro");
		zonaspar.put("Viseu","Centro");
		zonaspar.put("Guarda","Centro");
		zonaspar.put("Porto","Norte");
		zonaspar.put("Braga","Norte");
		zonaspar.put("Viana do Castelo","Norte");
		zonaspar.put("Vila Real","Norte");
		zonaspar.put("Bragança","Norte");
	}
		
	
	Map<String,String> zonasimpar = new HashMap<String,String>() ;
	{
		zonasimpar.put("Lisboa","Centro");
		zonasimpar.put("Leiria","Centro");
		zonasimpar.put("Santarém","Centro");
		zonasimpar.put("Castelo Branco","Centro");
		zonasimpar.put("Aveiro","Centro");
		zonasimpar.put("Coimbra","Centro");
		zonasimpar.put("Viseu","Centro");
		zonasimpar.put("Guarda","Centro");
		zonasimpar.put("Setúbal", "Sul");
		zonasimpar.put("Évora", "Sul");
		zonasimpar.put("Portalegre", "Sul");
		zonasimpar.put("Beja", "Sul");
		zonasimpar.put("Faro", "Sul");
	}
	
	private Map<String,String> regiao = new HashMap<String,String>() ;
	private String name;
	private int i = 1;
	private List<JobView> jobs = new ArrayList<JobView>();
	
	
	public TransporterPort (String name){
		int id =Integer.parseInt( name.substring(name.length()-1));
		this.name = name;
		if (id%2==0){
			regiao=zonaspar;
		}
		else{
			regiao=zonasimpar;
		}
		
	}
	
	@Override
	public String ping(String name) {
		
		MessageContext messageContext = webServiceContext.getMessageContext();

		messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);

		return name;
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {

			MessageContext messageContext = webServiceContext.getMessageContext();

			messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);
			
			if(zonaspar.containsKey(origin) == false && zonasimpar.containsKey(origin) == false){
				BadLocationFault faultInfo = new BadLocationFault();
				faultInfo.setLocation(origin);
				throw new BadLocationFault_Exception("Origem desconhecida: ", faultInfo);
			} 
			if(zonaspar.containsKey(destination) == false && zonasimpar.containsKey(destination) == false){
				BadLocationFault faultInfo = new BadLocationFault();
				faultInfo.setLocation(destination);
				throw new BadLocationFault_Exception("Destino desconhecido: ", faultInfo);
			} 
			if(price < 0){
				BadPriceFault faultInfo = new BadPriceFault();
				faultInfo.setPrice(price);
				throw new BadPriceFault_Exception("Preço inválido: ", faultInfo);
			}
				
			if(regiao.containsKey(origin) == false){
				return null;
			}
			
			if(regiao.containsKey(destination) == false){
				return null;
			}
			
			if(price > 100){
				return null;
			}
			
			int randomprice= 0;
			
			if(price <= 1){
				randomprice = 0;
			}
				
			if(price <= 10 && price > 1){
				Random random = new Random();
				 randomprice = random.nextInt(price-1) + 1;
			}
			
			
			if(price > 10 && price <= 100){
				 
				if(price%2 != 0 && regiao.equals(zonasimpar)){
					
					Random random = new Random();
					 randomprice = random.nextInt((price)-10) + 10;
			
				} 
				else if(price%2 == 0 && regiao.equals(zonaspar)){
					
					Random random = new Random();
					 randomprice = random.nextInt((price)-10) + 10;
				}else{
					
					Random random = new Random();
					 randomprice = random.nextInt(120-(price+1)) + (price+1);	
				}
			}
			
			JobView job = new JobView();
			job.setCompanyName(name);
			//Criar ID
			String Identifier = this.name + ":" + i;
			i++;
			job.setJobIdentifier(Identifier);
			job.setJobOrigin(origin);
			job.setJobDestination(destination);
			job.setJobPrice(randomprice);
			job.setJobState(JobStateView.PROPOSED);
			jobs.add(job);
			return job;	
	}
	

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		
		MessageContext messageContext = webServiceContext.getMessageContext();

		messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);
		
		JobView job = jobStatus(id);
		
		if(id == null || id.equals("")){
			BadJobFault faultInfo = new BadJobFault();
			faultInfo.setId(id);
			throw new BadJobFault_Exception("O trabalho com o seguinte indicador não existe ", faultInfo);
		}
		
		if(job.getJobState() == (JobStateView.ACCEPTED) || job.getJobState() == (JobStateView.REJECTED)){
			BadJobFault faultInfo = new BadJobFault();
			faultInfo.setId(id);
			throw new BadJobFault_Exception("O trabalho com o seguinte indicador não existe ", faultInfo);
		}
		
		if(job != null){
			if(accept == true){
				job.setJobState(JobStateView.ACCEPTED);
				timer(job);
			}else{
				job.setJobState(JobStateView.REJECTED);
			}
		}else{
			BadJobFault faultInfo = new BadJobFault();
			faultInfo.setId(id);
			throw new BadJobFault_Exception("O trabalho com o seguinte indicador não existe ", faultInfo);
		}
		
		return job;
	}

	@Override
	public JobView jobStatus(String id) {
		
		MessageContext messageContext = webServiceContext.getMessageContext();

		messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);
		
		if(id == (null)){
			return null;
		}
		
		for(JobView job: jobs){
			if(job.getJobIdentifier().equals(id)){
				return job;
			}
		}
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		
		MessageContext messageContext = webServiceContext.getMessageContext();

		messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);
		
		return jobs;
	}

	@Override
	public void clearJobs() {
		
		MessageContext messageContext = webServiceContext.getMessageContext();

		messageContext.put(HeaderHandler.CONTEXT_PROPERTY, this.name);
		
		jobs.clear();
		i = 1;
	}
	
	public void timer(JobView job) {
	       
		Random random1 = new Random();
		Random random2 = new Random();
		Random random3 = new Random();
		
        int temporandom1 = random1.nextInt(5000-1000) + 1000;

        int temporandom2 = random2.nextInt(5000-1000) + 1000;

        int temporandom3 = random3.nextInt(5000-1000) + 1000;

       
        timer1 = new Timer();
        timer2 = new Timer();
        timer3 = new Timer();
       
        timer1.schedule(new ChangeState(timer1, job), temporandom1);
        timer2.schedule(new ChangeState(timer2, job), temporandom2);
        timer3.schedule(new ChangeState(timer3, job), temporandom3);
    }

}
