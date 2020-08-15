package pt.upa.transporter.ws.it;

import static org.junit.Assert.*;

import org.junit.Test;

public class PingIT extends BaseTransporterIT{

	@Test
	public void test() throws Exception {
		final String result = port.ping("Teste");
		final String result2 = port2.ping("Teste");

		assertEquals("Teste", result);
		assertEquals("Teste", result2);
	}
	
	
}
