package edu.upenn.cis455.crawler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;






//import javax.lang.model.util.Elements;
import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.DOMException;
//import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

//import org.apache.commons.io.IOUtils;














import sun.net.util.URLUtil;
import edu.upenn.cis455.crawler.info.URLInfo;
import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.DBWrapper.Page;


public class XPathCrawler {
	
	private String dataURL;
	private int maxSize;
	private boolean fourthArg = false;
	private static int numFiles = -1;
	private static LinkedList<URLInfo> urlList = new LinkedList<URLInfo>();
	public boolean isHttps = false;
	private int filesCrawled = 0;
	
	int docCount = 0;
	//private URLInfo urlO;
	
	public static void main(String args[]) throws UnknownHostException, DOMException, IOException, ParseException{
		XPathCrawlerFactory fact = new XPathCrawlerFactory();
		XPathCrawler crawl = fact.getCrawler();
		//DBWrapper.init();
		crawl.setStartUrl(args[0]);
		crawl.setDataUrl(args[1]);
		System.out.println(crawl.dataURL);
		crawl.setMaxSize(Integer.parseInt(args[2]));
		if(args.length<2){//!args[3].equals("") && args[3]!=null){
			crawl.setNumFiles(Integer.parseInt(args[4]));
		}
		crawl.crawl();
		Page p =DBWrapper.getPageByURL(args[0]);
		System.out.println(p);
		//System.out.println(new BufferedReader(
			//	new InputStreamReader(DBWrapper.getPageByURL(args[0]).getIS())).readLine());
	}
//set methods
	public void setStartUrl(String start){
		addToLinks(start, null);
	}
	public void setDataUrl(String data){
		File file = new File(data);
		//if(!file.exists())
			//file.mkdirs();
		//dataURL = data;
		
		DBWrapper.init(data);
	}
	public void setMaxSize(int size){
		maxSize= size;
	}
	public void setNumFiles(int num){
		fourthArg = true;
		numFiles = num;
	}
	
// crawl
	public void crawl() throws UnknownHostException, DOMException, IOException, ParseException{
		while(!urlList.isEmpty()){
			URLInfo next=urlList.getLast();
			urlList.removeLast();
			parse(next);
			filesCrawled++;
		//	src = next;
		}
	}

// parse
	public void parse(URLInfo url) throws UnknownHostException, IOException, DOMException, ParseException{
		
		//urlO= new URLInfo(url)
		//System.out.println("\n\nSource: " +url.getFullURL()+"\tDoc#: "+ dcount);
		if((!fourthArg)||(filesCrawled<numFiles&&fourthArg)){
		if(checkHost(url)==true){	
		saveDoc(url.getFullURL());
		OutputStream os;
		InputStream is;
		HttpsURLConnection tempCon = null;
		Socket socket = null;
		if(url.isHttps()){
			URL temp = new URL(url.getFullURL());
			tempCon = (HttpsURLConnection)temp.openConnection();
			tempCon.setDoOutput(true);
			tempCon.setRequestProperty("User-Agent", "cis455crawler");
			os = tempCon.getOutputStream();
			//is = tempCon.getInputStream();
		}
		else{
			//set socket
			socket = new Socket(url.getHostName(), url.getPortNo());
			//In/Output Stream
			//is = socket.getInputStream();
			os = socket.getOutputStream();
		}
		
		PrintWriter pw = new PrintWriter(os);
		
		
		//Write Http request
		pw.println("GET "+ url.getFilePath() +" HTTP/1.1");
		pw.println("Host: "+ url.getHostName());
		pw.println("User-Agent: cis455crawler");
		pw.println();
		pw.flush();
		System.out.println("sss");
		if(url.isHttps()){
			is=tempCon.getInputStream();
		}
		else{
			is = socket.getInputStream();
		}
		
		Document doc = Jsoup.parse(is, "UTF-8", "test.html");
		Elements links=doc.select("a[href]");
		
		for(Element ahref: links){
			System.out.println( "link: "+  fixUrl(ahref.attr("href"),url));
			URLInfo toAdd = new URLInfo(fixUrl(ahref.attr("href"),url));
			urlList.addFirst(toAdd);
		}
		
		//JTidy
		//Tidy myTidy = new Tidy();
		//myTidy.setXmlOut(true);
		//Document doc =myTidy.parseDOM(is, null);
		//NodeList links =doc.getElementsByTagName("a");
		//System.out.println("sss"+links.toString());
		
		//add links to list
		//for(int i=0; i<links.getLength(); i++){
			//String link =links.item(i).getAttributes().getNamedItem("href").getNodeValue();
			//addToLinks(url);
			
		//}
		//tempCon.disconnect();
		//socket.close();
	}}}
	

	public XPathCrawler(){
		
	}
	//check if the host satisfys the conditions
	public boolean checkHost(URLInfo info) throws UnknownHostException, IOException, ParseException{
		//URLInfo  info = new URLInfo(url); 
		OutputStream os;
		InputStream is;
		HttpsURLConnection tempCon =null;
		Socket socket = null;
		
		if(info.isHttps()){
			isHttps=true;
			URL temp = new URL(info.getFullURL());
			tempCon = (HttpsURLConnection)temp.openConnection();
			tempCon.setRequestMethod("HEAD");
			tempCon.setRequestProperty("User-Agent", "cis455crawler");
			tempCon.setDoOutput(true);
			os = tempCon.getOutputStream();
			//is = tempCon.getInputStream();
		}
		else{
			/*URL temp = new URL(info.getFullURL());
			tempCon = (HttpsURLConnection)temp.openConnection();
			tempCon.setRequestMethod("HEAD");
			tempCon.setRequestProperty("User-Agent", "cis455crawler");
			tempCon.setDoOutput(true);
			os = tempCon.getOutputStream();*/
			socket = new Socket(info.getHostName(), info.getPortNo());
			//In/Output Stream
			//is = socket.getInputStream();
			os = socket.getOutputStream();
			
		}
				
		//Print Writet
		PrintWriter pw = new PrintWriter(os);
				
				
		//Write Http request
		pw.println("HEAD "+ info.getFilePath() +" HTTP/1.1");
		pw.println("Host: "+ info.getHostName());
		pw.println("User-Agent: cis455crawler");
		pw.println();
		pw.flush();
		//os.close();
		//System.out.println("#######"+tempCon.getHeaderField("Content-Length"));
		if(isHttps){
			
			
		//	System.out.println(tempCon.getHeaderFields());
		
			if( tempCon.getContentLength() !=-1&&
					tempCon.getContentLength() > (maxSize * 10 *10 *10* 10 *10 *10)){
				System.out.println();
				return false;
				}
			if(DBWrapper.getPageByURL(info.getFullURL())!=null){
				if(DBWrapper.getPageDate(info.getFullURL()).getTime() > tempCon.getHeaderFieldDate("Last-Modified", (long) -1.0)){
					return false;
				}
			}
			if(tempCon.getHeaderField("Content-Type").equals("text/html")){
				return true;
			}
			else if(tempCon.getHeaderField("Content-Type").endsWith("xml")){
				saveDoc(info.getFullURL());
				return false;
			}
			
		}
		else{
		if(info.isHttps()){
			is=tempCon.getInputStream();
		}
		else{
			is = socket.getInputStream();
		}
		System.out.println("br");
			BufferedReader br = new BufferedReader(
				new InputStreamReader(is));
//			System.out.println(br.readLine());
		String temp = "  ";
		socket.shutdownOutput();
		while((temp=br.readLine())!=null){
			//date
			System.out.println("---------"+temp);
			if(temp.startsWith("Date: ")){
				String dhead = temp.split(": ")[1];
				//DateFormat df = new SimpleDateFormat() 
			}
			//content-type
			else if(temp.startsWith("Content-Type: ")){
				String chead = temp.split(": ")[1];
				System.out.println("CL: "+chead);
				if(chead.startsWith("text/html")){
					
				}
				else if(chead.endsWith("xml")){
					saveDoc(info.getFullURL());
					return false;
				}
				else{
					return false;
				}
			}	
		}
		//br.close();}//end else
//		if(isHttps)
		//	tempCon.disconnect();
	//	if(!isHttps)
			//socket.close();
	}
		return true;
	}
	
	//save Doc to db
	public void saveDoc(String url) throws IOException{
		 
		docCount++;
		System.out.println("Doc#: "+docCount);
		URLInfo urlObj = new URLInfo(url);
		//HttpsURLConnection tempCon;
		numFiles++;
		OutputStream os;
		InputStream is;
		Socket socket = null;
		HttpsURLConnection tempCon = null;
		
		int contentLength = 100000;
		if(url.startsWith("https://")){
			URL temp = new URL(url);
			tempCon = (HttpsURLConnection)temp.openConnection();
			tempCon.setDoOutput(true);
			tempCon.setRequestProperty("User-Agent", "cis455crawler");
			os = tempCon.getOutputStream();
			contentLength = tempCon.getContentLength();
			is = tempCon.getInputStream();
		}
		else{
			//set socket
			socket = new Socket(urlObj.getHostName(), urlObj.getPortNo());
			//In/Output Stream
			is = socket.getInputStream();
			os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.println("GET "+ urlObj.getFilePath() +" HTTP/1.1");
			pw.println("Host: "+ urlObj.getHostName());
			pw.println("User-Agent: cis455crawler");
			pw.println();
			pw.flush();
			socket.shutdownOutput();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is));
//				System.out.println(br.readLine());
			String temp = "  ";
			System.out.println(br.readLine());//socket.shutdownOutput();
			while((temp=br.readLine())!=null){
				//date
				System.out.println("---------"+temp);
				if(temp.startsWith("Date: ")){
					String dhead = temp.split(": ")[1];
					//DateFormat df = new SimpleDateFormat() 
				}
				//content-type
				else if(temp.startsWith("Content-Length: ")){
					String chead = temp.split(": ")[1];
					System.out.println("CL: "+chead);
					contentLength = Integer.parseInt(chead);
				}	
			}
		}
		//Write Http request
		
	//	System.out.println( tempCon.getResponseCode());
		//is = tempCon.getInputStream();
		DataInputStream dIn = new DataInputStream(is);
		System.out.println(dIn.available());
		byte[] pass= new byte[contentLength];
		dIn.readFully(pass);
		Page temp = new Page(url, pass);
		DBWrapper.addPage(temp);
		System.out.println("))))");
		if(!isHttps)
			socket.close();
		else
			tempCon.disconnect();
		//tempCon.
	}
	public void addToLinks(String url, URLInfo src){
		synchronized(urlList){
		urlList.addFirst(new URLInfo((fixUrl(url, src))));
		}
	}
	public String fixUrl(String url, URLInfo src){
		if(src==null){
			return url;
		}
		System.out.println("url: "+ url +" parent "+ src.getFullURL());
		//URLInfo nNURL = new URLInfo(url);
		
		//URLInfo urlObj = new URLInfo(url);
		boolean sameHost = false;
		StringBuffer tempProt =new StringBuffer();
		
		
		if(src.isHttps() && !url.startsWith("https://")){
			tempProt.append("https://");
			//url = url.substring(8);
		}
		else if(!url.startsWith("http://")){
			tempProt.append("http://");
			//url = url.substring(7);
		}
		else if(url.startsWith("http://")){
			url = url.substring(7);
		}
		else{
			url = url.substring(8);
		}
		
		
		if(!url.startsWith("www.")&&src.hasWWW()){
			tempProt.append("www.");
		}
		else if(src.hasWWW()){
			url = url.substring(4);
		}
		if(!url.startsWith(src.getHostName())){
			tempProt.append(src.getHostName());
			tempProt.append(src.getFilePath());//url = url+src.getHostName();
			//url = url.substring(src.getHostName().length());
		}
		else{
			url = url.substring(src.getHostName().length());
		}
		
		//if(!url.startsWith())
		//if(!url.startsWith("/")){
			//tempProt.append('/');
			//url =url.substring(1);
			//System.out.println(tempProt.toString() +" : "+url);
		//}
		//tempProt.append(url);
		
		return tempProt.toString();
	}
	
}
