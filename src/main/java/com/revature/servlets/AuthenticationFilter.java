package com.revature.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.model.Employee;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter{
//	https://www.journaldev.com/1997/servlet-jdbc-database-connection-example

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest  req = (HttpServletRequest)  request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		if (uri.endsWith("recreate") || uri.endsWith("fill")) {
			chain.doFilter(req, res);
			return;
		}
		
		HttpSession session = req.getSession(false);
		if (session == null) {		
			if (uri.contains("session") || uri.endsWith("reimbursements/me") || uri.endsWith("employees")) {
				res.sendRedirect("/templates/login.html");
				System.out.println("Authentication: Can't access session pages without session");
				return;
			}
//			if (!(uri.equals("/") || uri.endsWith("login") || uri.endsWith("login.html") ||
//					uri.endsWith("register") || uri.endsWith("index.html"))) {
//				System.out.println(uri);
//				System.out.println("Authentication: Not logged in, redirecting to home...");
//				res.sendRedirect("templates/login.html");
//				return;
//			}
		}
		else {
			Object sessionData = session.getAttribute("Employee");
			if (sessionData == null) {
				res.sendRedirect("/templates/login.html");
				return;
			}
			if (!(sessionData instanceof Employee)) {
				res.sendRedirect("/templates/login.html");
				return;
			}
			Employee employee = (Employee) sessionData;
			
			if (employee.getIsManager()) {
				// TODO: Add employee gets, like reimbursements/me
				if (uri.contains("ePages")) {
					System.out.println("Authentication: Can't view as manager");
					res.sendRedirect("/templates/session/mPages/managerHome.html");
					return;
				}
			}
			else {
				// TODO: Add manager functions
				if (uri.contains("mPages")) {
					System.out.println("Authentication: Can't view as employee");
					res.sendRedirect("/templates/session/mPages/employeeHome.html");
					return;
				}
				
			}			
		}
		chain.doFilter(req, res);	
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
