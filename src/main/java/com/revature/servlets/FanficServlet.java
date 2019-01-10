package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.model.Fanfic;
import com.revature.services.FanficService;

/**
 * Servlet implementation class FanficServlet
 */
public class FanficServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FanficService fanficService = new FanficService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FanficServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter writer = response.getWriter();
		writer.write(fanficService.getAllFanfic().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		Fanfic newFanfic = new Fanfic(Integer.parseInt(id), title, body);
		fanficService.insertFanfic(newFanfic);
		
		
	}

}
