package example.ws.handler;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.upa.CA.ws.cli.CAClient;
import pt.upa.crypto.X509DigitalSignature;

/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String CONTEXT_PROPERTY = "my.property";

	final static String KEYSTORE_FILE = "UpaBroker/UpaBroker.jks";
	final static String KEYSTORE_PASSWORD = "ins3cur3";

	final static String KEY_ALIAS = "UpaBroker";
	final static String KEY_PASSWORD = "1nsecure";
	
	public static final String CLASS_NAME = HeaderHandler.class.getSimpleName();
	
	static Map<String, Certificate> certificados = new HashMap<String, Certificate>();
	static List<String> uuids = new ArrayList<String>();
	
    
    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("AddHeaderHandler: Handling message.");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        String propertyValue = (String) smc.get(CONTEXT_PROPERTY);
		System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);
		if(propertyValue == "UpaBroker1"){
			propertyValue = "UpaBroker";
		}

        try {
            if (outboundElement.booleanValue()) {
                System.out.println("Writing header in outbound SOAP message...");

                X509DigitalSignature x509 = null;
                
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                
                
                
                UUID uuid = UUID.randomUUID();
                
                byte[] uuidSignature = x509.makeDigitalSignature(uuid.toString().getBytes(), x509.getPrivateKeyFromKeystore(""+propertyValue+"/"+propertyValue+".jks",
                		KEYSTORE_PASSWORD.toCharArray(), propertyValue, KEY_PASSWORD.toCharArray()));
             
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                msg.writeTo(out);
                
                System.out.println("Mensagem a assinar: " + out);
                
                byte[] digitalSignature = x509.makeDigitalSignature(out.toByteArray(), x509.getPrivateKeyFromKeystore(""+propertyValue+"/"+propertyValue+".jks",
                		KEYSTORE_PASSWORD.toCharArray(), propertyValue, KEY_PASSWORD.toCharArray()));
                
                System.out.println("Assinatura: " + digitalSignature); 
                
             // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName("myHeader", "d", "http://demo");
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                element.addTextNode(printBase64Binary(digitalSignature));
                

                // add header element (name, namespace prefix, namespace)
                Name name2 = se.createName("nome", "n", "http://nome");
                SOAPHeaderElement element2 = sh.addHeaderElement(name2);
                
             // add header element value
                element2.addTextNode(propertyValue);
                System.out.println("-------------------");
                System.out.println(propertyValue);
                System.out.println("-------------------");
                
             // add header element (name, namespace prefix, namespace)
                Name name3 = se.createName("uuid", "u", "http://uuid");
                 SOAPHeaderElement element3= sh.addHeaderElement(name3);
                 element3.addTextNode(uuid.toString());

               // add header element (name, namespace prefix, namespace)
                 Name name4 = se.createName("myuuid", "m", "http://myuuid");
                 SOAPHeaderElement element4 = sh.addHeaderElement(name4);

                 // add header element value
                 element4.addTextNode(printBase64Binary(uuidSignature));
                
           
            } else {
                System.out.println("Reading header in inbound SOAP message...");

                X509DigitalSignature x509 = null;
                
                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();  

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                // get first header element
                Name name = se.createName("myHeader", "d", "http://demo");
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String valueString = element.getValue();
                byte[] value = parseBase64Binary(valueString);

                // print received header
                System.out.println("Header value is " + value);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, value);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);
                
             // get first2 header element
                Name name2 = se.createName("nome", "n", "http://nome");
                it = sh.getChildElements(name2);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element2 = (SOAPElement) it.next();

                // get header element value
                String valueString2 = element2.getValue();

                // print received header
                System.out.println("Header value is " + valueString2);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, valueString2);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);
                
             // get first2 header element
                Name name3 = se.createName("uuid", "u", "http://uuid");
                it = sh.getChildElements(name3);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element3 = (SOAPElement) it.next();

                // get header element value
                String uuid = element3.getValue();
                if(!uuids.contains(uuid)){
                	uuids.add(uuid);
                }else{
                	return false;
                }

                // print received header
                System.out.println("Header value is " + uuid);
                
                System.out.println("UUIDS: " + uuids);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, uuid);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);
                
                // get first header element
                Name name4 = se.createName("myuuid", "m", "http://myuuid");
                it = sh.getChildElements(name4);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element4 = (SOAPElement) it.next();

                // get header element value
                String myuuid = element4.getValue();
                byte[] myuuidvalue = parseBase64Binary(myuuid);

                // print received header
                System.out.println("Header value is " + myuuidvalue);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, myuuidvalue);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);
               
                
                Certificate certificado = null;
                
                if(!certificados.containsKey(valueString2)){
                	CAClient coisa = new CAClient("http://localhost:9090", "CA-ws");
                	Certificate cert = coisa.connection(valueString2);
                	if(cert == null){
                		return false;
                	}
                	certificados.put(valueString2, cert);
           
                } else{
                	certificado = certificados.get(valueString2);
                }
                
                sh.removeContents();
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                msg.writeTo(out);
                
                Boolean y = x509.verifyDigitalSignature(myuuidvalue, uuid.getBytes(), x509.getPublicKeyFromCertificate(certificado));
                if(y){
                	System.out.println("UID Válido");
                }
                else{
                	System.out.println("UID Inválido");
                	return false;
                }
                
                Boolean x = x509.verifyDigitalSignature(value, out.toByteArray(),  x509.getPublicKeyFromCertificate(certificado));
                if(x){
                	System.out.println("Mensagem Válida");
                }
                else{
                	System.out.println("Mensagem Inválida");
                	return false;
                }
                
            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }

}