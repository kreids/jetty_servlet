package edu.upenn.cis455.xpathengine;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class EqualsXPathEngineImplTest {
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
	public void testEquals() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body/chest[text()=\"hairy\"]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testEquals2() throws Exception{
		String[] test = new String[1];
		test[0]="/person/head[text()=\"\n\t\"]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}

	@Test
	public void testWhite() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body/chest[     text()=\"hairy\"]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testWhite2() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body/chest[te    xt()=\"hairy\"]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	@Test
	public void testWhite3() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body/chest[text()=\"hairy\"     ]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(true,testVal);
	}
	
	@Test
	public void testWhite4() throws Exception{
		String[] test = new String[1];
		test[0]="/person/body/chest[text()=\"hairy      \"]";
		engine.setXPaths(test);
		boolean testVal= engine.isValid(0);
		assertEquals(false,testVal);
	}

}
