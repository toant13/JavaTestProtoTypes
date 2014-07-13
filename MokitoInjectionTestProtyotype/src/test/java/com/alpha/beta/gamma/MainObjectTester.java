package com.alpha.beta.gamma;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Matchers;

import static org.junit.Assert.assertTrue;

import com.alpha.beta.delta.AncillaryObject;

@RunWith(MockitoJUnitRunner.class)
public class MainObjectTester {

	@Mock
	private AncillaryObject ancill;
	
	@InjectMocks
	private MainObject obj1 = new MainObject();
	@Mock
	private AncillaryObject ancill2;


	
	
	@Test
	public void runTests(){
		System.out.println("afsdfS");
		
		
		firstTest();
		secondTest();
	
	}
	
	public void secondTest(){
//		obj1 = Mockito.mock(MainObject.class);
		Mockito.when(ancill.TestMethod("sfsd")).thenReturn("dfsdfsdfsdfsd");
		String s = obj1.theEvent();
		System.out.println(s);
	}
	

	
	public void firstTest(){
		MainObject obj = new MainObject();
		Mockito.when(ancill.TestMethod("sfsd")).thenReturn("teatew");
		
		Mockito.doReturn("jlfasjdlkfs").when(obj).theEvent();
		
		
		String c = obj.theEvent();
		System.out.println(c);
		
		assertTrue("String is not empty", !c.isEmpty());
	}
}	
