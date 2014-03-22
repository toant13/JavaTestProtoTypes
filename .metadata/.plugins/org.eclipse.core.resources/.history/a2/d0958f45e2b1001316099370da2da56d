package mil.ssc.pac.jerseytest.test;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

public class RestTestIT {
	
	@Test
	public void getTest()
	{	
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI());
		String result = target.path("myresource").path("msg").request().get(String.class);
		System.out.println(result);	
		assertTrue("Failed string match",result.equals("Hi therdfasdfse!"));
	}
	
	private URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost:8080/").build();
	}
}
