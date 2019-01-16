package com.revature.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.dao.EmployeeDao;
import com.revature.model.Employee;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		String username = request.getParameter("name");
		String password = request.getParameter("Password");
		if (username == null || username == "" || password == null || password == "") {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
		}
		
		Employee employee = EmployeeDao.getEmployee(username, password);
		if (employee == null)
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		else {
			// create session, redirect?
			
			RequestDispatcher view = request.getRequestDispatcher("html/mypage.html");
			view.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
