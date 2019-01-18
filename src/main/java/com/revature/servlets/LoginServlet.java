package com.revature.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.revature.beans.LoginDataBean;
import com.revature.dao.EmployeeDao;
import com.revature.model.Employee;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Login started");
		response.setContentType("text/html");

		Map<String, String[]> params = request.getParameterMap();
		System.out.println("Count " + params.size());
		Iterator<String> i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			String value = ((String[]) params.get( key ))[0];
			System.out.println(String.format("Key: %s, value: %s", key, value));
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("user " + username);
		System.out.println("pass " + password);
		if (username == null || username == "" || password == null || password == "") {
			System.out.println("Bad login info");
			response.sendRedirect("templates/login.html");
			// Return response item
		}
		else {
			Employee employee = EmployeeDao.getPerson(username, password);
			if (employee == null) {
				System.out.println("Employee doesn't exist");
				response.sendRedirect("templates/login.html");
				// return response item (type and message)
			}			
			else {
				// create session, redirect?
				HttpSession session = request.getSession();
				session.setAttribute("Employee", employee);
				
				String homePage = "templates/session/";
				homePage += employee.getIsManager() ? "mPages/managerHome.html" : "ePages/employeeHome.html";
				System.out.println("Redirecting to " + homePage);
				response.sendRedirect(homePage);
			}
		}		
	}
}
