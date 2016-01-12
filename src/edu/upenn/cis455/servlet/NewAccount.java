package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;

public class NewAccount extends HttpServlet {
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
			out.println("<title>SignUP</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<form method=\"post\">");
			out.println("user name: <input type=\"text\" name=\"usr\"><br>");
			out.println("password: <input type=\"text\" name=\"pass\"><br>");
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
			String name = request.getParameter("usr");
			String pass = request.getParameter("pass");
			DBWrapper.addUsr(name, pass);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>SignUP</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("Signed Up");
			out.println("<br><a href =\"Login\">Login</a>");
			out.println("</body>");
			out.println("</html>");
			
			
		}



}
