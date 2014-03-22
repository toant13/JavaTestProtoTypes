package ssc.pac.jtnc.jnm;

import java.io.File;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


public class ClientTest 
{
	public static void main(String[] args) 
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		WebResource service = client.resource(getBaseURI());
		FormDataMultiPart multiPart = new FormDataMultiPart();
		
		File commplan;
		//ClientResponse response = service.path("CommPlan").path("commPlanRec").path("name").type("application/xml").accept(MediaType.APPLICATION_JSON).entity(CommPlan).post(ClientResponse.class);

	}
	
	private static URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost:8080/").build();
	}
}
