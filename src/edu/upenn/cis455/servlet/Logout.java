package edu.upenn.cis455.servlet;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.storage.DBWrapper;

public class Logout extends HttpServlet{
	public void init() {
		DBWrapper.init(getServletContext()
				.getInitParameter("BDBstore"));
				this.getServletConfig().getServletContext().setAttribute("env",DBWrapper.myEnv);
		
		}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	//request.getSession().invalidate();
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
		if (c.getName().equals("user")) {
		c.setMaxAge(0); // delete cookie
		response.addCookie(c);
		}
		}
	}

}
