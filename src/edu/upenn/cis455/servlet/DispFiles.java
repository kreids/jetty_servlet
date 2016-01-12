package edu.upenn.cis455.servlet;

import java.util.Iterator;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.DBWrapper.Page;

public class DispFiles {
	
	
	static PrimaryIndex<String,Page> pageI;
	

	public static void main(String[] args){
		DBWrapper.init("database");
		Iterator<Page> it =DBWrapper.getPageIt().iterator();
		while(it.hasNext()){
			System.out.println("Date: "+it.next().getDate());
		}
	}
}
