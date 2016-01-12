package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.DBWrapper;

public class Index extends HttpServlet{

public void init() {
	DBWrapper.init(getServletContext()
			.getInitParameter("BDBstore"));
			this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
}

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
	out.println("<a href>");
	out.println("</body>");
	out.println("</html>");
	response.setStatus(200);
	out.flush();
}


}
