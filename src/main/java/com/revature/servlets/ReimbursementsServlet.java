package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.revature.beans.ReimbursementsBean;
import com.revature.beans.ResolvedPairBean;
import com.revature.dao.ReimbursementDao;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;

@WebServlet(name="Reimbursements", urlPatterns= {"/reimbursements/me", "/reimbursements"})
public class ReimbursementsServlet extends HttpServlet {
	private static final long serialVersionUID = -5725324897120985096L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		System.out.println("Reim servlet path " + servletPath);
		switch(servletPath) {
		case "/reimbursements/me":
			getMyPendingAndResolvedRequests(request, response);			
			break;
		case "/reimbursements":
			getAllPendingAndResolvedRequests(request, response);
			break;
		}
	}	
	
	protected void getMyPendingAndResolvedRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Starting get Pending/resolved");
		HttpSession session = request.getSession(false);
		if (session == null)
			response.sendRedirect("templates/login.html");		
		Employee employee = (Employee) session.getAttribute("Employee");
		if (employee == null)
			response.sendRedirect("templates/login.html");
		System.out.println(String.format("myId: %s", employee.getId()));
		
		List<Reimbursement> myPending = ReimbursementDao.getEmployeePendingReimbursements(employee.getId());
		List<Reimbursement> myResolved = ReimbursementDao.getEmployeeResolvedReimbursements(employee.getId());	
		ReimbursementsBean myRequests = new ReimbursementsBean(myPending, ReimbursementDao.getResolversForReimbursements(myResolved));
		// todo fix
		ConnectionServlet.writeToJson(request, response, myRequests);
		System.out.println("Got pending/resolved");
	}
	protected void getAllPendingAndResolvedRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Rest: Getting Pending/resolved");
		List<Reimbursement> allPending = ReimbursementDao.getAllPendingReimbursementsAsManager();
		List<Reimbursement> allResolved = ReimbursementDao.getAllResolvedReimbursementsAsManager();
		List<ResolvedPairBean> pairs = ReimbursementDao.getResolversForReimbursements(allResolved);
		for (ResolvedPairBean pair : pairs) {
			System.out.println(String.format("%s vs %s", pair.getReimbursement().toString(), pair.getResolver().toString()));
		}
		ReimbursementsBean myRequests = new ReimbursementsBean(allPending, pairs);
		ConnectionServlet.writeToJson(request, response, myRequests);
		System.out.println("Got pending/resolved");
	}

}
