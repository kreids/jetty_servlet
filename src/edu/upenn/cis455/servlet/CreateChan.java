package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;

public class CreateChan extends HttpServlet {
	
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
		response.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>New Chan</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method=\"post\">");
		out.println("name: <input type=\"text\" name=\"name\"><br>");
		out.println("xpath: <input type=\"text\" name=\"xpath\"><br>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//user interface
		PrintWriter out = response.getWriter();
		//out.println("HTTP/1.1 100 OK\r");
		//out.println("Content-type: text/html\r");
		//out.println("\r");
		String name = request.getParameter("name");
		String xpath = request.getParameter("xpath");
		//DBWrapper.addUsr(name, pass);
		out.println("<html>");
		out.println("<head>");
		out.println("<title>SignUP</title>");
		out.println("</head>");
		out.println("<body>");
		
		String usr=null;
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
		if (c.getName().equals("user")) 
			usr = c.getValue();
		if(usr!=null){
			DBWrapper.addChannel(usr, name, xpath);
			out.println("Successfully Created Channel");}
		out.println("</body>");
		out.println("</html>");
		return;
		}
		//System.out.println(usr);
		
	
			out.println("Login to Create a Channel");
		out.println("</body>");
		out.println("</html>");
		
		
	}
	
}