package pt.upa.CA.ws;

import java.io.IOException;
import java.security.cert.Certificate;

import javax.jws.WebService;

@WebService
public interface CA {

	String sayHello(String name);
	byte[] downloadCertificate(String certificateFile) throws IOException;

}
