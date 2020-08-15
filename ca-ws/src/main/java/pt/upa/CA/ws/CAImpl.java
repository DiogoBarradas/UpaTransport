package pt.upa.CA.ws;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.CA.ws.CA")
public class CAImpl implements CA {

	public String sayHello(String name) {
		return "Hello " + name + "!";
	}
	
	public byte[] downloadCertificate (String fileName) {
        String filePath = fileName;
        System.out.println("Sending file: " + filePath);
         
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            byte[] fileBytes = new byte[(int) file.length()];
            inputStream.read(fileBytes);
            inputStream.close();
             
            return fileBytes;
        } catch (IOException ex) {
            System.err.println(ex);
            return null; 
        }
          
    }
	
}
