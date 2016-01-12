package edu.upenn.cis455.storage;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBWrapperTest2 {


	@Before
	public void setUp() throws Exception {
		String current = new java.io.File( "." ).getCanonicalPath();
		DBWrapper.init("db");
	}

	@After
	public void tearDown() throws Exception {
		DBWrapper.close();
	}
	
	@Test
	public void testGet() {
		DBWrapper.addPage("Page", new byte[8]);
		assertEquals(8,DBWrapper.getPageByURL("Page").getBytes().length);
	}

	@Test
	public void testGetDate() {
		DBWrapper.addPage("Page", new byte[8]);
		assertEquals(19,DBWrapper.getPageByURL("Page").getDate().length());
	}
	@Test
	public void testGetDate2() throws ParseException {
		DBWrapper.addPage("Page", new byte[8]);
		assertNotNull(DBWrapper.getPageDate("Page"));
	}
}
