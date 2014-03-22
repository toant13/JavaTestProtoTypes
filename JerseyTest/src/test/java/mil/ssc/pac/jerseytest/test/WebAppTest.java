package mil.ssc.pac.jerseytest.test;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import mil.ssc.pac.jerseytest.MyResource;
import mil.ssc.pac.jerseytest.test.psuedoapp.PsuedoApplication;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebAppTest {
	
	
	
	
	
	
	@Test
	public void test(){
		MyResource m = new MyResource();
		assertTrue("getTest failed cause return didn't match", m.getIt().equals("Hi therdfasdfse!"));
	}
	
	

}
