package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.xpathengine.XPathEngineFactory;
import edu.upenn.cis455.xpathengine.XPathEngineImpl;

public class DispChan extends HttpServlet{
	
	
	public void init() {
		DBWrapper.init(getServletContext()
				.getInitParameter("BDBstore"));
				this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
		
		}
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//user interface
		PrintWriter out = response.getWriter();
		//out.println("HTTP/1.1 100 OK\r");
		//out.println("Content-type: text/html\r");
		//out.println("\r");
		out.println("<html>");
		out.println("<head>");
		out.println("<title></title>");
		out.println("</head>");
		out.println("<body>");
		
		
		
		//System.out.println(new BufferedReader(
		//	new InputStreamReader(DBWrapper.getPageByURL(args[0]).getIS())).readLine());
		int id= Integer.valueOf(request.getParameter("cid")).intValue();
		DBWrapper.Channel chan =DBWrapper.getChanByID(id);
		
		
		out.println("Name: "+ chan.getChanName() +"<br>");
		out.println("<a href=\'DeleteChan?cid="+id+"\'>Delete</a><br>");
		Iterator<String> xPath = chan.getXP().iterator();
		while(xPath.hasNext()){
			out.println("XPath" + xPath.next()+"<br><br>");
		}
		
		Iterator<DBWrapper.Page> pageIt = DBWrapper.getPageIt().iterator();
		while(pageIt.hasNext())
		{
			DBWrapper.Page p = pageIt.next();
			HashSet<String> xps =chan.getXP();
			String[] arr= xps.toArray(new String[0]);
			System.out.println(":::"+arr[0]);
			try {
				XPathEngineImpl e = (XPathEngineImpl) XPathEngineFactory.getXPathEngine();
				e.setXPaths(arr);
				System.out.println(p.getDate());
				e.parseXML(p.getIS());
				boolean[] truths = e.evaluate();
				boolean notTrue = true;
						
				for(int i=0;i<truths.length;i++){
					if(truths[i]==true)
						notTrue=false;
				}
				if(notTrue==false){
					out.println("<br><hr><br>Crawled on:" + p.getDate());
					out.println("<br>Location: " +p.getURL());
					out.println("<br><br><div>"
							+ p.getText()+"</div><br>");
				}
			} catch (ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.getIS();
		}
		
		
		out.println("</body>");
		out.println("</html>");
		
	}

}
