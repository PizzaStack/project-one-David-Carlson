package com.revature.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.revature.dao.EmployeeDao;
import com.revature.model.Employee;



//@WebServlet(value="/database", loadOnStartup=1)
@WebServlet(name = "Database", urlPatterns = {"/database", "/database/recreate", "/database/fill", "/database/fill/*"}, loadOnStartup=1)
public class ConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger logger;
	private static Connection connection;
	
	public static Connection getConnection() {
		if (connection != null)
			return connection;
		else
			throw new RuntimeException("Connection null");
	}
	
	@Override
	public void init() {
		// https://stackoverflow.com/questions/2161054/where-to-place-and-how-to-read-configuration-resource-files-in-servlet-based-app
		// https://grokonez.com/java/java-read-write-properties-object-properties-file-properties-file
		System.out.println("ConnectionServlet init started");
//		logger = Logger.getLogger(ConnectionServlet.class);
//		logger.debug("Hello? !!!");
		String filepath = "C:/Users/Owner/Desktop/Company/Revature/project-one/connection.properties";
		Properties prop = new Properties();
		try (InputStream inputStream = new FileInputStream(filepath)) {
			
			if (inputStream == null)
				throw new IOException("inputStream null");
			prop.load(inputStream);
			if (prop == null)
				throw new IOException("prop null");
//				prop.forEach((k, v) -> System.out.println(String.format("key = %s, value = %s", k, v)));
			String hostname = prop.getProperty("db.hostname");
			String database = prop.getProperty("db.database");
			String port = prop.getProperty("db.port");
			String user = prop.getProperty("db.user");
			String password = prop.getProperty("db.password");
			if (hostname == null || database == null || port == null || user == null || password == null)
				throw new InvalidParameterException();
			
			String connURL = "jdbc:postgresql://" + hostname + ":" + port + "/" + database + "?user=" + user + "&password=" + password;
//			System.out.println("ConnURL: " + connURL);
			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connURL);
			System.out.println("Connection created!");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Can't load jdbc: " + e.toString());
		}
		catch (FileNotFoundException e1) {
			System.out.println("Can't find connection file");
		}catch (IOException e1) {
			System.out.println("IOException opening connection: " + e1.toString());
		}
		catch (InvalidParameterException e) {
			System.out.println("connection.properties value is null");
		}
		catch (SQLException e) {
			System.out.println("Can't create connection: " + e.toString());
		}
	}
	
	@Override
	public void destroy() {
		try {
			if (connection != null)
				connection.close();
		}
		catch(SQLException e) {
			System.out.println("Error closing connection: " + e.toString());
		}
		// https://stackoverflow.com/questions/3320400/to-prevent-a-memory-leak-the-jdbc-driver-has-been-forcibly-unregistered
        // This manually deregisters JDBC driver, which prevents Tomcat 7 from complaining about memory leaks wr/to this class
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                System.out.println(String.format("Error deregistering driver %s", driver));
            }
        }
	}
	
	public static void recreateDatabase() throws SQLException {
		System.out.println("Starting recreateDatabase");
		String[] dropStatements = { 
				"drop table IF EXISTS reimbursement_items;",
				"drop table IF EXISTS reimbursements;", 
				"drop table IF EXISTS employees;"};

		String[] createStatements = { 
				"create table employees ("
				+ "id serial primary key, "
				+ "is_manager boolean not NULL, "
				+ "username varchar(50) unique, "
				+ "password varchar(50) not NULL,"
				+ "firstname varchar(50) not NULL, "
				+ "lastname varchar(50) not NULL);",		

				"create table reimbursements (" 
				+ "id serial primary key, " 
				+ "user_id serial references employees(id), "
				+ "state varchar(20) not NULL, "
				+ "resolved_by serial references employees(id));",
				// Add byte array image
				
				"create table reimbursement_items (" 
				+ "id serial primary key, " 
				+ "req_id serial references reimbursements(id), "
				+ "item_name varchar(50) not NULL,"
				+ "item_price real not NULL);",
				};
		try (Statement statement = connection.createStatement()) {
			
			for (String drop : dropStatements) {
				try {
					statement.executeUpdate(drop);
				}
				catch(SQLException e) {
					System.out.println(String.format("Can't drop: %s \n %s", drop, e.toString()));
				}				
			}
			for (String create : createStatements) {
				try {
					statement.executeUpdate(create);
				}
				catch(SQLException e) {
					System.out.println(String.format("Can't Create: %s \n %s", create, e.toString()));
				}
			}
			System.out.println("Created databases");
		} 
		catch (Exception e) {
			System.err.println("Problem recreating databases");
			throw e;
		}		
	}
	
	public static void fillDatabase() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("fillDatabase not written");
	}	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Conn doGet started");

		String servletPath = request.getServletPath();
		PrintWriter out = response.getWriter();
		out.append("Served at: ").append(request.getContextPath());

		System.out.println(servletPath + " is servletPath");
		try {
			switch(servletPath) {
			case "/database/recreate":
				recreateDatabase();
				out.append("did recreate");
				break;
			case "/database/fill":
				fillDatabase();
				out.append("did fill");
				break;
			default:
				System.out.println("Unrecognized connection path");
			}
		}
		catch(SQLException e) {
			System.out.println("Error doing " + servletPath + " " + e.toString());
		}			
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Conn doPost started");

		String servletPath = request.getServletPath();
		String extra = request.getPathInfo();
		PrintWriter out = response.getWriter();
		out.append("Served at: ").append(servletPath+extra);
		
		System.out.println(servletPath + " is servletPath");
		System.out.println(extra + " is extra");
		switch(servletPath + extra) {
//		case "/database/recreate":
//			recreateDatabase();
//			out.append("no recreate");
//			break;
		case "/database/fill/employee":
			addEmployee(request, response);
			break;
		default:
			System.out.println("Unrecognized connection path");
		}	
	}
	
	protected void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Boolean is_manager = Boolean.parseBoolean(request.getParameter("is_manager"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		System.out.println(String.format("%s %s %s %s %s", is_manager, username, password, firstName, lastName));
		Employee employee = new Employee(is_manager, username, password, firstName, lastName);
		if (!EmployeeDao.addEmployee(employee))
			System.out.println(employee.getUsername() + " wasn't added");
		else				
			response.getWriter().append("did fill " + employee.toString());
	}

}
