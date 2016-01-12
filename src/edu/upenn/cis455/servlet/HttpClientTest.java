package edu.upenn.cis455.servlet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis455.xpathengine.XPathEngineImpl;

public class HttpClientTest {
	
	HttpClient client;
	@Before
	public void setUp() throws Exception {
		client = new HttpClient("http://www.stackoverflow.com");
	}

	@After
	public void tearDown() throws Exception {
		client.closeAll();
	}

	@Test
	public void testGetHost() throws IOException {
		String host = client.getHostName();
		
		assertEquals("www.stackoverflow.com", host);
	}
	
	//@Test
	/*public void testGetPage() throws IOException{
		//client.initPage();
		String text= client.getPage();
		assertEquals(true, text.contains("<head>"));
	}*/
	
	@Test
	public void testGetInputStream() throws IOException{
		InputStream temp = client.getInputStream();
		//client.initPage();
		BufferedReader br = new BufferedReader(new InputStreamReader(temp));
		String line;
		String whole="";
		while((line=br.readLine())!=null){
			whole = whole+ line;
			System.out.println(line);
		}
		assertEquals(true, whole.contains("<head>"));
	}
	
	@Test
	public void testToXpathEngine() throws IOException, ParserConfigurationException{
		XPathEngineImpl test = client.toXPathEngine();
		assertEquals(false, test.isSAX());
	}
	@Test
	public void testGetContentType() throws IOException{
		//client.initPage();
		String type= client.getContentType();
		assertEquals(type,"text/html");
	}
	

}
