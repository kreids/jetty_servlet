package edu.upenn.cis455.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.utilint.Timestamp;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import edu.upenn.cis455.storage.DBWrapper.Usr;

public class DBWrapper {
	// usr
	@Entity
	public static class Usr{
		@PrimaryKey
		private String usrName;
		
		private String passWrd;
		
		//@SecondaryKey(relate =Relationship.ONE_TO_MANY, relatedEntity=Usr.class, onRelatedEntityDelete=DeleteAction.NULLIFY)
		private HashSet<DBWrapper.Channel> channels;
		
		//constructors
		public Usr (String name, String pass){
			usrName = name;
			passWrd = pass;
			channels = new HashSet<Channel>();
		}
		public Usr(){
			
		}
		// methods
		public boolean login(String pass){
			return passWrd.equals(pass);
		}
		public void addChannel(Channel chan){
			channels.add(chan);
		}
		public HashSet<DBWrapper.Channel> getChannels(){
			return channels;
			
		}
		
		public String getName(){
			return usrName;
		}
		
	}
	//channel
	@Entity
	public static class Channel{
		@PrimaryKey(sequence="chan_seq")
		private int channelID;
		@SecondaryKey(relate =Relationship.MANY_TO_ONE, relatedEntity=Usr.class, onRelatedEntityDelete=DeleteAction.CASCADE)
		private String usrName;
		//channel name
		private String name="untitled";
		private HashSet<String> xpaths=new HashSet<String>();
		
		// constructors
		public Channel(){
		}
		public Channel(Usr usr){
			usrName = usr.getName();
			usr.addChannel(this);
		}
		public Channel(Usr usr, String chanName){
			usrName = usr.getName();
			name=chanName;
			usr.addChannel(this);
		}
		public Channel(Usr usr, String chanName, String xpath){
			usrName = usr.getName();
			name=chanName;
			usr.addChannel(this);
			xpaths.add(xpath);
		}
		// methods
		public String getChanName(){
			return name;
		}
		public HashSet<String> getXP(){
			return xpaths;
			
		}
		public String getUser(){
			return usrName;
		}
		public int getId(){
			return channelID;
		}
		
	}
	@Entity
	public static class Page{
		@PrimaryKey
		private String url;
		
		private byte[] data;
		
		public byte[] getBytes(){
			return data;
		}
		
		private Date date;
		//Constructor
		public Page(){
			date = new Date();
		}
		public Page(String url, byte[] data){
			this.url = url;
			this.data = data;
			date = new Date();
		}
		public String getDate(){
			DateFormat df =new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss");
			return df.format(date);
		}
		public InputStream getIS(){
			return new ByteArrayInputStream(data); 
		}
		public String getText() throws UnsupportedEncodingException{
			return new String(data,"UTF-8");
		}
		public String getURL(){
			return url;
		}
		
		
	}
	
	
	
	
	
	private static String envDirectory = null;
	public static Environment myEnv;
	public static EntityStore store;
	
	
//user stuff
	//add a user to the database
	public static void addUsr(String name, String pass){
		PrimaryIndex<String, Usr> tempIndex = store.getPrimaryIndex(String.class, Usr.class);
		Usr tempUser = new DBWrapper.Usr(name, pass);
		tempIndex.put(tempUser);
	}
	//Delete a user
	public static void deleteUsr(String name){
			PrimaryIndex<String, Usr> tempIndex= store.getPrimaryIndex(String.class, Usr.class);
			tempIndex.delete(name);
	}
	//get a username from the database
	public static String getUsrName(String name){
		PrimaryIndex<String, Usr> tempIndex = store.getPrimaryIndex(String.class, Usr.class);
		String rVal=tempIndex.get(name).getName();
		return rVal;
	}
	// get a user object from the db
	public static Usr getUsr(String name){
		PrimaryIndex<String, Usr> tempIndex = store.getPrimaryIndex(String.class, Usr.class);
		Usr rVal=tempIndex.get(name);
		return rVal;
	}
	//login
	public static boolean login(String name, String pass){
		PrimaryIndex<String, Usr> tempIndex = store.getPrimaryIndex(String.class, Usr.class);
		return tempIndex.get(name).login(pass);
	}
	
	
	
//channel stuff
	//add a channel
	public static void addChannel(Usr user){
		PrimaryIndex<Integer, Channel> tempIndex = store.getPrimaryIndex(Integer.class, Channel.class);
		Channel tempChan = new DBWrapper.Channel(user);
		tempIndex.put(tempChan);
	}
	public static void addChannel(String user){
		Usr usr = getUsr(user);
		addChannel(usr);	
	}
	public static void addChannel(Usr user, String chanName){
		PrimaryIndex<Integer, Channel> tempIndex = store.getPrimaryIndex(Integer.class, Channel.class);
		Channel tempChan = new DBWrapper.Channel(user, chanName);
		tempIndex.put(tempChan);
	}
	
	public static void addChannel(String user, String chanName, String path){
		PrimaryIndex<Integer, Channel> tempIndex = store.getPrimaryIndex(Integer.class, Channel.class);
		Usr nuser= getUsr(user);
		Channel tempChan = new DBWrapper.Channel(nuser, chanName, path);
		tempIndex.put(tempChan);	
	}
	//Delete a user
	public static void deleteChan(int ind){
				PrimaryIndex<Integer, Channel> tempIndex= store.getPrimaryIndex(Integer.class, Channel.class);
				tempIndex.delete(ind);
		}
	//get channel list
	public static HashSet<Channel> getChannelsByUser(Usr user){
		return user.getChannels();
		
	}
	//get channel by id
	public static Channel getChanByID(int id){
		PrimaryIndex<Integer, Channel> tempIndex = store.getPrimaryIndex(Integer.class, Channel.class);
		Channel rVal=tempIndex.get(id);
		return rVal;
	}
	//get all channels
	public static HashSet<Channel> getAllChannels(){
		HashSet<Channel> rVal = new HashSet<Channel>();
		PrimaryIndex<Integer, Channel> tempIndex= store.getPrimaryIndex(Integer.class, Channel.class);
		EntityCursor<Channel> curs=tempIndex.entities();
		Iterator<Channel> iter =curs.iterator();
		while(iter.hasNext()){
			rVal.add(iter.next());
		}
		return rVal;
	}
	
//webpage stuff
	public static void addPage(Page page){
		PrimaryIndex<String, Page> tempIndex = 
				store.getPrimaryIndex(String.class, Page.class);
		Page tempPage = page;
		tempIndex.put(tempPage);
		//store.closeClass(page.getClass());
	}
	public static void addPage(String url, byte[] data){
		Page tempPage = new Page(url, data);
		addPage(tempPage);
	}
	//get page
	public static Page getPageByURL(String url){
		PrimaryIndex<String, Page> tempIndex = store.getPrimaryIndex(String.class, Page.class);
		Page rVal=tempIndex.get(url);
		return rVal;
	}
	public static HashSet<Page> getPageIt(){
	HashSet<Page> rVal = new HashSet<Page>();
	PrimaryIndex<String, Page> tempIndex= 
			store.
			getPrimaryIndex(
					String.class,
					Page.class);
	EntityCursor<Page> curs=tempIndex.entities();
	Iterator<Page> iter =curs.iterator();
	while(iter.hasNext()){
		rVal.add(iter.next());
	}
	
	return rVal;
	
	}
	//get Date 
	public static Date getPageDate(String url) throws ParseException{
		Page temp = getPageByURL(url);
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		return df.parse(temp.getDate());
	}
	
	
	private static void deleteDir(File dir){
		System.out.println();
		  File[] contents = dir.listFiles();
		  //System.out.println(contents[0].toString());
		  for(File file: contents){
			  if(file.isDirectory())
				  deleteDir(file);
			  else
				  file.delete();
		  }
		  dir.delete();
	}
	
	//initialize a new database
	public static void init(String dir){
		//set dir
		envDirectory = dir;
		File file = new File(dir);
		//if(file.exists())
			//deleteDir(file);
		if(!file.exists())
			file.mkdir();
		
		
		//enviroment
		EnvironmentConfig tempECon = new EnvironmentConfig();
		tempECon.setTransactional(true);
		tempECon.setAllowCreate(true);
		myEnv = new Environment(new File(dir), tempECon);
		//store
		StoreConfig tempSCon = new StoreConfig();
		tempSCon.setAllowCreate(true);
		tempSCon.setTransactional(true);
		store = new EntityStore(myEnv, "check", tempSCon);	
	}
	
	//close the database
	public static void close(){
		store.close();
		myEnv.close();
	}
}