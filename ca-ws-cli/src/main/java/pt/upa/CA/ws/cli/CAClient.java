package pt.upa.CA.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.upa.crypto.X509DigitalSignature;
import pt.upa.crypto.X509CertificateCheck;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.ca.ws.CA;
import pt.upa.ca.ws.CAImplService;

public class CAClient {
	
	private String uddiURL;
	private String name;	
	
	public CAClient(String url, String nome){
		uddiURL = url;
		name = nome;
	}

	public Certificate connection(String nome) {

		String filepath = null;
		

		try{
			
			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
	
			System.out.printf("Looking for '%s'%n", name);
			String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			//return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		} 

		System.out.println("Creating stub ...");
		CAImplService service = new CAImplService();
		CA port = service.getCAImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		System.out.println("Remote call ...");
		InputStream is = new ByteArrayInputStream(port.downloadCertificate(""+nome+"/"+nome+".cer"));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert = cf.generateCertificate(is);
		
		
		X509DigitalSignature assinaturaX509 = null;
		X509CertificateCheck checkCertificateX509 = null;
		
		
		String path= "ca-certificate.pem.txt";
		Certificate chavePublica = assinaturaX509.readCertificateFile(path);
		PublicKey Key = assinaturaX509.getPublicKeyFromCertificate( chavePublica );
		
		Boolean valid = checkCertificateX509.verifySignedCertificate(cert, Key);
		if(valid)
			return cert;
		else
			return null;
		
		} catch(Exception e){
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();
		}
		return null;
		
	}
}
