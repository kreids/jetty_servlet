package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;

public class DeleteChan extends HttpServlet {
	
	public void init() {
		DBWrapper.init(getServletContext()
				.getInitParameter("BDBstore"));
				this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
				//userDB = env.getUserDatabase();
				//channelDB = env.getChannelDatabase();
		
		}
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
		int cid =Integer.valueOf(request.getParameter("cid")).intValue();
		DBWrapper.Channel chan = DBWrapper.getChanByID(cid);
		
		

		String usr=null;
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
		if (c.getName().equals("user")) 
			usr = c.getValue();}
		if(usr!=null &&usr.equals(chan.getUser())){
			DBWrapper.deleteChan(cid);
			out.println("Successfully Deleted Channel");}
		else
			out.println("Delete failed");
		out.println("</body>");
		out.println("</html>");
	}
}
