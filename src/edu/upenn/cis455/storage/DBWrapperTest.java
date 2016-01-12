package edu.upenn.cis455.storage;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBWrapperTest {

	@Before
	public void setUp() throws Exception {
		String current = new java.io.File( "." ).getCanonicalPath();
		DBWrapper.init("db2");
	}

	@After
	public void tearDown() throws Exception {
		DBWrapper.close();
	}

	@Test
	public void testAddUser() {
		DBWrapper.addUsr(new String("Steve"), "Steve");
		assertEquals(DBWrapper.getUsrName("Steve"), "Steve");
	}
	
	@Test
	public void testLogin() {
		DBWrapper.addUsr("Steve", "Stevie");
		assertEquals(DBWrapper.login("Steve", "Stevie"), true);
	}
	
	@Test
	public void testLogin2() {
		DBWrapper.addUsr("Steve", "Stevie");
		assertEquals(DBWrapper.login("Steve", "Steve"), false);
	}
	
	@Test
	public void testAddChan() {
		DBWrapper.addUsr("Steve", "Stevie");
		DBWrapper.Usr testUser = DBWrapper.getUsr("Steve");
		DBWrapper.addChannel(testUser, "_test_");
		String temp=((DBWrapper.Channel)DBWrapper.getChannelsByUser(testUser).toArray()[0]).getChanName();
		assertEquals("_test_", temp);
	}
	
	@Test
	public void testDellUsr(){
		DBWrapper.addUsr("Steve", "Stevie");
		DBWrapper.deleteUsr("Steve");
		DBWrapper.Usr testUser = DBWrapper.getUsr("Steve");
		assertEquals(null, testUser);
	}
	

}
