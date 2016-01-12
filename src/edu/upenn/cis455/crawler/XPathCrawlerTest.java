package edu.upenn.cis455.crawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis455.storage.DBWrapper;

public class XPathCrawlerTest {

	XPathCrawler crawler;
	
	@Before
	public void setUp() throws Exception {
		crawler = new XPathCrawler();
		crawler.setStartUrl("https://dbappserv.cis.upenn.edu/crawltest.html");
		crawler.setMaxSize(Integer.MAX_VALUE);
		crawler.setDataUrl("db");
	}

	@After
	public void tearDown() throws Exception {
	}
	/*@Test
	public void testCheckHost2() throws UnknownHostException, IOException, ParseException {
		DBWrapper.addPage("https://dbappserv.cis.upenn.edu/crawltest.html", new byte[8]);
		//boolean test =crawler.checkHost("https://dbappserv.cis.upenn.edu/crawltest.html");
		assertEquals(false, test);
		
	}*//*
	@Test
	public void testCheckHost() throws UnknownHostException, IOException, ParseException {
		boolean test =crawler.checkHost("https://dbappserv.cis.upenn.edu/crawltest.html");
		assertEquals(true, test);
	}*/

	

}
