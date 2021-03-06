package mil.ssc.pac.jerseytest.proxy;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

public class SecondJerseyConnector {
	public String getTestMsg(){
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI());
		String result = target.path("second").path("msg").request().get(String.class);
		
		return "Messgae from other app: " + result;
	}
	
	private URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost:8081/").build();
	}
}
