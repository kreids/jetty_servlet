package edu.upenn.cis455.servlet;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import edu.upenn.cis455.xpathengine.XPathEngineFactory;
import edu.upenn.cis455.xpathengine.XPathEngineImpl;

public class HttpClient{
	
	/**
	 * 
	 */
	
	
	
	private HashMap<String, String> headers;
	private URL url;
	private Socket sockToSend;
	private String host;
	private PrintWriter pW;
	private String response;
	private BufferedReader reader;
	private boolean builtEngine=false;
	XPathEngineImpl engine;
	//private boolean pageInit =false;
	
	public HttpClient(String url) throws UnknownHostException, IOException{
		this.url=new URL(url);
		host = this.url.getHost();
		//System.out.println(InetAddress.getByName(host));
		//System.out.println(this.url.getPath());
		sockToSend = new Socket(InetAddress.getByName(host),80);
		
		pW = new PrintWriter(sockToSend.getOutputStream());
		if(this.url.getPath()!=null)
			pW.println("GET "+ this.url.getPath()+ " HTTP/1.1");
		else
			pW.println("GET / HTTP/1.1");
		pW.println("Host: "+ host);
		pW.println();
		pW.flush();
		initPage();
	}
	
	public String getHostName() throws IOException{
		return url.getHost();
		
	}
	
	
	// initialize headers && page
	public void initPage() throws IOException{
		//reader = new BufferedReader(new InputStreamReader(sockToSend.getInputStream()));
		response="";
		String temp;
		boolean isHeader =true;
		headers = new HashMap<String, String>();
		boolean getString = true;
		parseOutHeaders(sockToSend.getInputStream());
		/*while((temp=reader.readLine())!=null){
			if(isHeader && temp.equals("")){
				isHeader = false;
			}
			// put in headers
			else if(isHeader && !getString){
				System.out.println(temp);
				String[] head = temp.split(": ");
				headers.put(head[0], head[1]);
			}
			//don't try to put in first line
			else{
				getString =false;
			}
			
			if(!isHeader){
			//	System.out.println(temp);
				//response = response+ temp;
			}
		}*/
	}
	
	public void parseOutHeaders(InputStream is)
	        throws IOException {
	    int currChar;
	    String str = "";
	    while (true) {
	    	currChar= is.read();
	        str = str + (char)currChar; //add char to string
	        if ((char) currChar == '\r') { 
	            str = str+(char)is.read(); //add \n
	            currChar = is.read();
	            if (currChar == '\r') {  
	                str= str+(char)is.read();
	                break; //\r\n\r\n indicates end of headers
	            } else {
	                str= str+(char)currChar;
	            }
	        }
	    }
	    //send headers to hashmap
	    String[] headsArr =str.split("\r\n");
	    headers = new HashMap<>();
	    for (int i = 1; i < headsArr.length - 1; i++) {
	        headers.put(headsArr[i].split(": ")[0],
	                headsArr[i].split(": ")[1]);
	    }
	}


	
	public String getPage() throws IOException{
		return response;
	}
	public String getContentType() throws IOException{
		return headers.get("Content-Type").split(";")[0];
	}
	
	public InputStream getInputStream() throws IOException{
		return sockToSend.getInputStream();
	}
	
	public XPathEngineImpl toXPathEngine() throws ParserConfigurationException{
		builtEngine=true;
		engine= (XPathEngineImpl) XPathEngineFactory.getXPathEngine();
		return engine;
	}
	
	public void buildDomTree() throws ParserConfigurationException, SAXException, IOException{
		if(!builtEngine)
			toXPathEngine();
		if(headers.get("Content-Type").startsWith("text/html"))
			engine.parseHTML(sockToSend.getInputStream());
		else	
			engine.parseXML(sockToSend.getInputStream());
		engine.getDoc().toString();
	}
	
	public void buildDomTreeX() throws ParserConfigurationException, SAXException, IOException{
		if(!builtEngine)
			toXPathEngine();
		engine.parseXML(sockToSend.getInputStream());
		engine.getDoc().toString();
	}
	
	public Document getDoc(){
		return engine.getDoc();
	}
	
	public boolean[] evaluate(){
		return engine.evaluate();
	}
	
	public void setXPaths(String[] xPaths){
		engine.setXPaths(xPaths);
	}
	
	
	public void closeAll() throws IOException{

		//reader.close();
		pW.close();
		sockToSend.close();		
	}
	
	
	

}
