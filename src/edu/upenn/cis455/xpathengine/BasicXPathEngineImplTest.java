package edu.upenn.cis455.xpathengine;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;


public class BasicXPathEngineImplTest {
	
	XPathEngine engine;

	@Before
	public void setUp() throws Exception {
		engine= XPathEngineFactory.getXPathEngine();
		File persxml = new File("person.xml");
		((XPathEngineImpl) engine).parseXML(persxml);
		
	}
	 public void tearDown() {
		engine = null;
	 }

	@Test
	public void testIsSax() throws Exception{
		assertEquals(engine.isSAX(),false);
	}
	@Test
	public void testAxisStep() throws Exception{
		String[] test = new String[1];
		test[0]="/person/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	@Test
	public void testAxisStep2() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	@Test
	public void testAxisStep3() throws Exception{
		String[] test = new String[1];
		test[0]="/person/head/eyes";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testTest() throws Exception{
		String[] test = new String[1];
		test[0]="/person[body]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testTest2() throws Exception{
		String[] test = new String[1];
		test[0]="/person[body]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}

	
	@Test
	public void testWhite() throws Exception{
		String[] test = new String[1];
		test[0]="/person[body]/   head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	@Test
	public void testWhite2() throws Exception{
		String[] test = new String[1];
		test[0]="/     person[body]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	@Test
	public void testWhite3() throws Exception{
		String[] test = new String[1];
		test[0]="    /person[body]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testWhite4() throws Exception{
		String[] test = new String[1];
		test[0]="    /pers    on[body]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(false,testVal);
	}
	@Test
	public void testWhite5() throws Exception{
		String[] test = new String[1];
		test[0]="/person[     body]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testWhite6() throws Exception{
		String[] test = new String[1];
		test[0]="/person[body      ]/head";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
}