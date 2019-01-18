package com.revature.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.revature.model.Employee;

@WebServlet(value="/hello")
public class HelloServlet extends HttpServlet{
//	
	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee employee = new Employee(0, true, "usery", "passsy", "firsty", "namey");
		Employee employee2 = new Employee(1, false, "usery2", "passsy2", "firsty2", "namey2");
		List<Employee> emps = new ArrayList<Employee>();
		emps.add(employee); 
		emps.add(employee2);
		String empString = new Gson().toJson(emps);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(empString);
		out.flush();
		
//		response.sendRedirect("http://www.google.com");
	}	
	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("In doPost");
		BufferedReader br = request.getReader();
		String line = "";
		while((line=br.readLine()) != null) {
			System.out.println(line);
		}
		
		//		String name = request.getParameter("name");
//		String password = request.getParameter("password");
		PrintWriter output = response.getWriter();
//		output.write("POST: " + name + " " + password);
		output.write("wtf");
	}
	
//	@GET
//	public Response getOne() {
//		String output = "Gottem";
//		return Response.status(200).entity(output).build();
//	}
//	@GET
//	@Path("{param}")
//	public Response getShit(@PathParam("param") String msg) {
//		String output = "say: " + msg;
//		return Response.status(200).entity(output).build();
//	}
//	
//	@POST
//	@Path("/add")
//	public Response AddUser(
//			@FormParam("id") int id, 
//			@FormParam("name") String name,
//			@FormParam("price") float price) {
//		System.out.println("In add");
//		return Response.status(200)
//				.entity(String.format("Product added <br> %s %s %s", id, name, price))
//				.build();
//	}
//	
	
	
//	@Override 
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String title = request.getParameter("fanficTitle");
//		PrintWriter output = response.getWriter();
//		output.write("POST: " + title);
//	}		
	
	
//	@Override 
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter output = response.getWriter();
////		String user = this.getServletContext().getInitParameter("user");
//		String user = this.getInitParameter("user");
//		if(request.getParameter("name") != null)
//			user = request.getParameter("name");
//		
//		HttpSession session = request.getSession();
//		session.setAttribute("currentUser", new String("Anakin"));
//		
//		output.write("GET " + user);
//		
//		response.sendRedirect("http://www.google.com");
//	}	
//	
//	@Override 
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String title = request.getParameter("fanficTitle");
//		PrintWriter output = response.getWriter();
//		output.write("POST: " + title);
//	}		
	
}
