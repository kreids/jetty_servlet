package edu.upenn.cis455.xpathengine;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XPathEngineImpl implements XPathEngine {

	private String[] xPaths;
	private DocumentBuilder db;
	private Document doc;
	private String currXPath;
	private int bracksToEscp = 0;
	private boolean beginingOfTest = false;

	public XPathEngineImpl() {
		// Do NOT add arguments to the constructor!!
	}

	public void setXPaths(String[] s) {
		xPaths = s;
	}
	
	//test method
	public boolean test(Node node){
		currXPath = currXPath.substring(1);		
		boolean tempBool =nextMethod(node);
		if(tempBool== true){
			return nextMethod(node);
		}
		
		return false;
	}
	
	
	//step method
	public boolean step(Node node){
		
		
		int slash = currXPath.indexOf('/');
		int brack = currXPath.indexOf('[');
		int backBrack = currXPath.indexOf(']');
		
		// -1 screws up comparing
		if(slash==-1){
			slash=Integer.MAX_VALUE;
		}
		if(brack==-1){
			brack=Integer.MAX_VALUE;
		}
		if(backBrack==-1){
			backBrack=Integer.MAX_VALUE;
		}
		
		String tag= currXPath;
		if(slash<brack && slash<backBrack){
			tag= tag.substring(0,slash);
			currXPath = currXPath.substring(slash);
		}
		else if(brack<slash && brack<backBrack){
			tag= tag.substring(0,brack);
			currXPath = currXPath.substring(brack);
		}
		else if(backBrack<slash && backBrack<brack){
			tag= tag.substring(0,backBrack);
			currXPath = currXPath.substring(backBrack);
		}
		
		else{
			currXPath = null;
		}
		String oldXPath =currXPath;
		tag = tag.trim();
		//boolean rVal= false;
		for(int i =0; i<node.getChildNodes().getLength(); i++){
			//check if xml tag from querrie is equal to node name
			if (tag.equals(node.getChildNodes().item(i).getNodeName())){
				Node tempNode = node.getChildNodes().item(i);
				
				//to exit
				if(currXPath !=null &&currXPath.replaceAll("\\s","").charAt(0)==']'){
					currXPath = currXPath.substring(currXPath.indexOf(']'));
					return true;
				}
				else{
					if(nextMethod(tempNode)==true){
						return true;
					}
					else{
						currXPath = oldXPath;
						//System.out.println(currXPath);
					}
				}
			}
		}
		return false;
	}
	
	
	//axis method
	public boolean axis(Node node){
		currXPath = currXPath.substring(currXPath.indexOf('/')+1);
		return step(node);
	}
	
	
	//attribute method
	public boolean attribute(Node node){
		String attributeExp = currXPath.substring(0, currXPath.indexOf(']'));
		//extract text between quotes
		String quote = attributeExp.split("\"")[1];
		// extract attribute name
		String attribute = attributeExp.substring(attributeExp.indexOf("@")+ 1,attributeExp.indexOf("=") ).trim();
		//System.out.println(quote+ " " + attribute);
		
		//fix Xpath
		currXPath = currXPath.substring(currXPath.indexOf("]"));
		//loop through attribute
		for(int i = 0; i<node.getAttributes().getLength(); i++){
			//System.out.println(node.getAttributes().item(i).getNodeName() + " "+attribute);
			//System.out.println(node.getAttributes().item(i).getNodeValue()+" "+quote);
			if(node.getAttributes().item(i).getNodeName().equals(attribute) 
					&& node.getAttributes().item(i).getNodeValue().equals(quote)){
				return true;
			}
		}
		return false;
	}
	
	
	//contains method
	public boolean contains(Node node){
		String containsExp = currXPath.substring(0, currXPath.indexOf(']'));
		String text=getText(node);
		//extract text from quotes
		String checkText = containsExp.split("\"")[1];
		currXPath = currXPath.substring(currXPath.indexOf("]"));
		return text.contains(checkText);
	}
	
	//text()= method
	public boolean equality(Node node){
		String equalsExp = currXPath.substring(0, currXPath.indexOf(']'));
		String text=getText(node);
		//extract text from quotes
		String checkText = equalsExp.split("\"")[1];
		currXPath = currXPath.substring(currXPath.indexOf("]"));
		return text.equals(checkText);
	}
	
	//pick next method
	public boolean nextMethod(Node node){
		//base case
		if(currXPath==null||currXPath.equals("")){
			return true;
		}
		
		//first part of test
		else if(beginingOfTest==true){
			beginingOfTest= false;
			if(currXPath.replaceAll("\\s","").startsWith("@")){
				return attribute(node);
			}
			else if (currXPath.replaceAll("\\s","").startsWith("contains")){
				return contains(node);
			}
			else if(currXPath.replaceAll("\\s","").startsWith("text()")){
				return equality(node);
			}
			else{
				return step(node);
			}
		}
		//axis
		else if(currXPath.replaceAll("\\s","").charAt(0)=='/'){
			return axis(node);
		}
		//test start
		else if(currXPath.replaceAll("\\s","").charAt(0)=='['){
			bracksToEscp++;
			beginingOfTest =true;
			return test(node);
		}
		//test end
		else if(currXPath.replaceAll("\\s","").charAt(0)==']'){
			bracksToEscp--;
			currXPath= currXPath.substring(1);
			return nextMethod(node);
		}
		
		
		return false;
	}
	
	
	public boolean isValid(int i) {
		currXPath= xPaths[i];
		return nextMethod(doc);
	}

	public boolean[] evaluate(Document d) {
		boolean[] right = new boolean[xPaths.length];
		for(int i=0; i<xPaths.length; i++){
			right[i] =isValid(i);
		}
		return right;
	}
	public boolean[] evaluate(){
		return evaluate(doc);
		
	}
	
	//get text from a node
	public String getText(Node node){
		return node.getTextContent();
	}

	@Override
	public boolean isSAX() {
		return false;
	}

	@Override
	public boolean[] evaluateSAX(InputStream document, DefaultHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//method for setting document builder
	public void setDocumentBuilder(DocumentBuilder db){
		this.db=db;
	}
	
	//method for parsing
	public void parseXML(File file) throws SAXException, IOException{
		doc = db.parse(file);
		doc.normalizeDocument();
		
	}
	public void parseXML(InputStream is) throws SAXException, IOException{
		doc = db.parse(is);
		doc.normalize();
	}
	public void parseHTML(InputStream is){
		Tidy tidy = new Tidy();
        //tidy.setInputEncoding("UTF-8");
        //tidy.setOutputEncoding("UTF-8");
        //tidy.setWraplen(Integer.MAX_VALUE);
        //tidy.setPrintBodyOnly(true);
        tidy.setXmlOut(true);
        //tidy.setSmartIndent(true);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(HTML.getBytes("UTF-8"));
        
        doc = tidy.parseDOM(is, null);
        
        //int i = 1;
		
	}
	
	//normalize internal stuct
	public void normalize(){
		doc.normalizeDocument();
	}
	
	// for testing
	public void setXPath(String string) {
		xPaths = new String[1];
		xPaths[0] =string;
	}
	public Document getDoc(){
		return doc;
	}



}
