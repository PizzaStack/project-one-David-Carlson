package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.revature.dao.EmployeeDao;
import com.revature.model.Employee;

@WebServlet(name="Employees", urlPatterns= {"/employees", "/employees/add"})
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		String extra = request.getPathInfo();
		System.out.println(servletPath + " servletPath");
		System.out.println(extra + " extra");
		
		switch(servletPath) {
		case "/employees":
			List<Employee> employees = EmployeeDao.getAllPeople();
			ConnectionServlet.writeToJson(request, response, employees);
			return;		
		}
	}	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		String extra = request.getPathInfo();
		System.out.println(servletPath + " servletPath");
		System.out.println(extra + " extra");
		switch(servletPath) {
		case "/employees/add":
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
			default:
				response.getWriter().append("No post on employees");
		}
		
	}
}
