package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.DBWrapper.Channel;

public class ListChannels extends HttpServlet {
	
	public void init() {
		DBWrapper.init(getServletContext()
				.getInitParameter("BDBstore"));
				this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
		
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
		Iterator<Channel> chans = DBWrapper.getAllChannels().iterator();
		while(chans.hasNext()){
			Channel chan = chans.next();
			out.println("<a href=\'DispChan?cid="+chan.getId() +"\'>"+
		chan.getChanName()+"</a><br>");
		}
		out.println("</body>");
		out.println("</html>");
	}
}
