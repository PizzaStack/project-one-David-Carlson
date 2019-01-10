package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HelloServlet extends HttpServlet {
	
	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter output = response.getWriter();
//		String user = this.getServletContext().getInitParameter("user");
		String user = this.getInitParameter("user");
		if(request.getParameter("name") != null)
			user = request.getParameter("name");
		
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", new String("Anakin"));
		
		output.write("GET " + user);
		
//		response.sendRedirect("http://www.google.com");
	}	
	
	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("fanficTitle");
		PrintWriter output = response.getWriter();
		output.write("POST: " + title);
	}		
	
}
