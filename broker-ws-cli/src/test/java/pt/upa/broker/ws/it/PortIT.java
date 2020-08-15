package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PortIT extends BaseBrokerIT {

	@Test
	public void test() throws Exception {
		final String result = port.ping("Teste");

		assertEquals("A fazer ping", result);
	}
	
}
