package mil.ssc.pac.jerseytest.test;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import mil.ssc.pac.jerseytest.test.psuedoapp.PsuedoApplication;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RestTestIT {
	
	
	
	
	
	@BeforeClass
	public static void setup() throws InterruptedException{		
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try {
					
					URI baseUri = UriBuilder.fromUri("http://localhost/").port(8081).build();
					ResourceConfig config = ResourceConfig.forApplicationClass(PsuedoApplication.class);
					Server server = JettyHttpContainerFactory.createServer(baseUri, config);
					server.join();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}			
		};
		new Thread(runnable).start();
		Thread.sleep(3000);
	}
	
	
	@Test
	public void getTest()
	{	
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI());
		String result = target.path("myresource").path("msg").request().get(String.class);
		System.out.println(result);	
		assertTrue("Failed string match",result.equals("Hi therdfasdfse!"));
	}
	
	@Test
	public void getConnector()
	{	
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI());
		String result = target.path("myresource").path("connect").request().get(String.class);
		System.out.println(result);	
		assertTrue("Failed string match",result.equals("Messgae from other app: Hi second jersey!"));
	}
	
	private URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost:8080/").build();
	}
}
