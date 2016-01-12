package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import edu.upenn.cis455.storage.DBWrapper;

@SuppressWarnings("serial")
public class XPathServlet extends HttpServlet {
	
	/* TODO: Implement user interface for XPath engine here */
	
	/* You may want to override one or both of the following methods */

	
	public void init() {
		
		
		}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");
		String xPath1= request.getParameter("XPath1");
		String xPath2 = request.getParameter("XPath2");
		PrintWriter out = response.getWriter();
		
		//logic & shiii
		HttpClient client = new HttpClient(url);
		String[] xPaths = new String[2];
		xPaths[0]= xPath1;
		xPaths[1]= xPath2;
		client.setXPaths(xPaths);
		boolean[] right = client.evaluate();

		
		
		
		//out.println("HTTP/1.1 100 OK\r");
		//out.println("Content-type: text/html\r");
		//out.println("\r");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>results</title>");
		out.println("</head>");
		out.println("<body>");
		for(int i=0; i<right.length; i++){
			if(right[i]==true)
				out.println("XPath" + (i+1) +" was correct");
			else
				out.println("XPath" + (i+1) +" was incorrect");
		}
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//user interface
		
		
		PrintWriter out = response.getWriter();
		
		//out.println("HTTP/1.1 100 OK\r");
		//out.println("Content-type: text/html\r");
		//out.println("\r");
		
		response.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>XPath Engine</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method=\"post\">");
		out.println("URL: <input type=\"text\" name=\"url\"><br>");
		out.println("XPath 1: <input type=\"text\" name=\"XPath1\"><br>");
		out.println("XPath 2: <input type=\"text\" name=\"XPath2\"><br>");		
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}

}









