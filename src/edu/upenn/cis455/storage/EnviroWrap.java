package edu.upenn.cis455.storage;

import java.io.File;

import com.sleepycat.je.EnvironmentConfig;

public class EnviroWrap {
	
	
	String dir;
	
	public EnviroWrap(String dir){
		this.dir = dir;
		File envPath = new File(dir);
		// if the directory does not exist, create it
		if (!envPath.exists()) {
		System.out.println("creating directory: " + dir);
		boolean result = false;
		try{
		envPath.mkdirs();
		result = true;
		}
		catch(SecurityException se){
		//handle it
		}
		if(result) {
		System.out.println(dir + " created");
		}
	}
		EnvironmentConfig myEnvConf = new EnvironmentConfig();
		// Configure the Environment
		myEnvConf.setTransactional(true);
		myEnvConf.setAllowCreate(true);
	}
}
