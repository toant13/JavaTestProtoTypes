package mil.ssc.pac.jerseytest.test;

import mil.ssc.pac.jerseytest.MyResource;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class WebAppTest {
	
	@Test
	public void test(){
		MyResource m = new MyResource();
		assertTrue("getTest failed cause return didn't match", m.getIt().equals("Hi therdfasdfse!"));
	}
	
	

}
