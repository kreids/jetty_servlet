package edu.upenn.cis455.xpathengine;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Implement this factory to produce your XPath engine
 * and SAX handler as necessary.  It may be called by
 * the test/grading infrastructure.
 * 
 * @author cis455
 *
 */
public class XPathEngineFactory {
	
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public static XPathEngine getXPathEngine() throws ParserConfigurationException {
		DocumentBuilder builder = factory.newDocumentBuilder();
		XPathEngineImpl rval = new XPathEngineImpl();
		rval.setDocumentBuilder(builder);
		return rval;
	}
	
	public static DefaultHandler getSAXHandler() {
		return null;
	}
	
	public static void main(String args[]) throws ParserConfigurationException{
		XPathEngine engine= XPathEngineFactory.getXPathEngine();
		
	}
}
