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
		System.out.println("Requested: " + uri);
		HttpSession session = req.getSession(false);
		if (session == null) {
			if (uri.contains("session")) {
				res.sendRedirect("index.html");
				return;
			}
		}
		else {
			Employee employee = (Employee) session.getAttribute("Employee");
			if (employee.getIsManager()) {
				if (uri.contains("mPage")) {
					
				}
			}
			else {
				
			}
		}
		
		if ( false ) {
			System.out.println("Unauthorized access");
		}
		else 
			chain.doFilter(request, response);
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
