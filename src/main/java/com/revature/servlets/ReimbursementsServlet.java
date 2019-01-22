package com.revature.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.revature.beans.ReimbursementsBean;
import com.revature.beans.ResolvedPairBean;
import com.revature.dao.ReimbursementDao;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;

@MultipartConfig
@WebServlet(name="Reimbursements", urlPatterns= {"/reimbursements/me", "/reimbursements", "/reimbursements/add"})
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
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		switch(servletPath) {
		case "/reimbursements/add":
			addReimbursement(request, response);
			response.sendRedirect("/templates/session/ePages/employeeHome.html");
			break;
		}
	}
	protected void addReimbursement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Reim: Starting addReimbursement");
		String item_name = request.getParameter("item_name");
		String item_price_str = request.getParameter("item_price");
		Part receiptPart = request.getPart("receipt-image");
		InputStream initialStream = receiptPart.getInputStream();
		byte[] receipt = ByteStreams.toByteArray(initialStream);
		HttpSession session = request.getSession(false);
		
		if (item_name == null || item_price_str == null || session == null) 
			return;
		Employee me = (Employee) session.getAttribute("Employee");
		if (me == null)
			return;
		try {
			Double item_price = Double.valueOf(item_price_str);
			Reimbursement reim = new Reimbursement(me.getId(), Reimbursement.Pending, item_name, item_price, 0, receipt);
			ReimbursementDao.addReimbursement(reim);
			System.out.println("Reim: Added reimbursement: " + reim.toString());
		}
		catch(Exception e) {
			return;
		}
	}
	
	protected void getMyPendingAndResolvedRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Reim: Starting get Pending/resolved");
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect("/templates/login.html");	
			return;
		}
				
		Employee employee = (Employee) session.getAttribute("Employee");
		if (employee == null)
			response.sendRedirect("/templates/login.html");
		System.out.println(String.format("myId: %s", employee.getId()));
		
		List<Reimbursement> myPending = ReimbursementDao.getEmployeePendingReimbursements(employee.getId());
		List<Reimbursement> myResolved = ReimbursementDao.getEmployeeResolvedReimbursements(employee.getId());	
		ReimbursementsBean myRequests = new ReimbursementsBean(myPending, ReimbursementDao.getResolversForReimbursements(myResolved));
		// todo fix
		ConnectionServlet.writeToJson(request, response, myRequests);
		System.out.println("Reim: Got pending/resolved");
	}
	protected void getAllPendingAndResolvedRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Reim:: Getting all Pending/resolved");
		List<Reimbursement> allPending = ReimbursementDao.getAllPendingReimbursementsAsManager();
		List<Reimbursement> allResolved = ReimbursementDao.getAllResolvedReimbursementsAsManager();
		List<ResolvedPairBean> pairs = ReimbursementDao.getResolversForReimbursements(allResolved);
		for (ResolvedPairBean pair : pairs) {
			System.out.println(String.format("%s vs %s", pair.getReimbursement().toString(), pair.getResolver().toString()));
		}
		ReimbursementsBean myRequests = new ReimbursementsBean(allPending, pairs);
		ConnectionServlet.writeToJson(request, response, myRequests);
		System.out.println("Reim: Got all pending/resolved");
	}

}
