package edu.upenn.cis455.servlet;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XPathServletTest {

	HttpClient client;
	@Before
	public void setUp() throws Exception {
		client = new HttpClient("http://toastytech.com/evil/");
	}

	@After
	public void tearDown() throws Exception {
		client.closeAll();
	}

	@Test
	public void testBuildDomTree() throws ParserConfigurationException, SAXException, IOException {
		client.buildDomTree();
		Document testDoc = client.getDoc();
		
		assertEquals(testDoc.getChildNodes().item(1).getNodeName(),"html");
	}
	
	@Test
	public void testBuildDomTree2() throws ParserConfigurationException, SAXException, IOException {
		client.buildDomTree();
		Document testDoc = client.getDoc();		
		assertEquals(testDoc.getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName(),"center");
	}

}
