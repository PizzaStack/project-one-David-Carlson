package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.revature.dao.EmployeeDao;
import com.revature.model.Employee;

@WebServlet(name="Personnel", urlPatterns= {"/personnel", "/personnel/employees", "/personnel/employees/me", "/personnel/managers", "/personnel/add"})
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		String extra = request.getPathInfo();
		System.out.println(servletPath + " servletPath");
		System.out.println(extra + " extra");
		
		switch(servletPath) {
		case "/personnel":
			ConnectionServlet.writeToJson(request, response, EmployeeDao.getAllPeople());
			return;		
		case "/personnel/employees":
			ConnectionServlet.writeToJson(request, response, EmployeeDao.getAllEmployees());
			return;		
		case "/personnel/employees/me":
			HttpSession session = request.getSession(false);
			if (session == null) {
				response.sendRedirect("/templates/login.html");	
				return;
			}
			Employee employee = (Employee) session.getAttribute("Employee");
			ConnectionServlet.writeToJson(request, response, employee);
			return;
			
		case "/personnel/managers":
			ConnectionServlet.writeToJson(request, response, EmployeeDao.getAllManagers());
			return;		
		}
	}	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		String extra = request.getPathInfo();
//		System.out.println(servletPath + " servletPath");
//		System.out.println(extra + " extra");
		switch(servletPath) {
		case "/personnel/add":
			try {
				Boolean is_manager = Boolean.parseBoolean(request.getParameter("is_manager"));
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				System.out.println(String.format("%s %s %s %s %s", is_manager, username, password, firstName, lastName));
				Employee employee = new Employee(is_manager, username, password, firstName, lastName);
				if (EmployeeDao.addEmployee(employee))
					System.out.println("Added employee");
			}
			catch(Exception e) {
				System.out.println("Error adding employee: " + e.toString());
			}
			break;
		case "/personnel/employees/me":
			System.out.println("Employees: Updating me");
			HttpSession session = request.getSession(false);
			if (session == null) {
				response.sendRedirect("/templates/login.html");	
				return;
			}
			Employee me = (Employee) session.getAttribute("Employee");
			String newUsername = request.getParameter("username-input");
			String newFirstname = request.getParameter("first-name-input");
			String newLastname = request.getParameter("last-name-input");
			if (newUsername != null && newFirstname != null && newLastname != null) {
				Employee newEmployee = new Employee(me.getId(), me.getIsManager(),newUsername, me.getPassword(), newFirstname, newLastname);
				if (EmployeeDao.updateEmployee(newEmployee)) {
					session.setAttribute("Employee", newEmployee);
					System.out.println("Employees: Update success");
				}
					
			}
			response.sendRedirect("/templates/session/ePages/employeeViewInfo.html");
			System.out.println("Employees: Done updating me");
			return;
		default:
			response.getWriter().append("No post on employees");
		}
		
	}
}
