package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.EnviroWrap;

public class Login extends HttpServlet{
	
	
	public void init() {
		DBWrapper.init(getServletContext()
				.getInitParameter("BDBstore"));
				this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
		
		}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//DBWrapper.init("dbt1");
		//DBWrapper.addUsr("steve", "steve");
		//user interface
		PrintWriter out = response.getWriter();
		//out.println("HTTP/1.1 100 OK\r");
		//out.println("Content-type: text/html\r");
		//out.println("\r");
		response.setContentType("text/html");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Login</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method=\"post\">");
		out.println("user name: <input type=\"text\" name=\"usr\"><br>");
		out.println("password: <input type=\"text\" name=\"pass\"><br>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		response.setStatus(200);
		out.flush();
		
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
		DBWrapper.Usr user = DBWrapper.getUsr(name);
		if(user!=null){
		if(user.login(pass)){
			Cookie login = new Cookie("user", name);
			login.setMaxAge(-1); // session persistent
			response.addCookie(login);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Logged In</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("Logged in");
			out.println("<br><a href =\"CreateChan\">Make a new Channel</a>");
			out.println("</body>");
			out.println("</html>");
			//request.getSession().setAttribute("name", name);
			
		}}
		else
			out.println("not logged in");
	}
	
}
