package com.revature.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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


@WebServlet(value="/database", loadOnStartup=1)
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
	}
	
	@GET
	@Path("/recreate")
	public void recreateDatabase() throws SQLException {
		String[] dropStatements = { 
				"drop table IF EXISTS reimbursement_items;",
				"drop table IF EXISTS reimbursements;", 
				"drop table IF EXISTS managers;",
				"drop table IF EXISTS employees;"};

		String[] createStatements = { 
				"create table employees ("
				+ "id serial primary key, "
				+ "username varchar(50) unique, "
				+ "password varchar(50),"
				+ "firstname varchar(50), "
				+ "lastname varchar(50), "
				+ "social varchar(9));",
				
				"create table managers ("
				+ "id serial primary key, "
				+ "username varchar(50) unique, "
				+ "password varchar(50),"
				+ "firstname varchar(50), "
				+ "lastname varchar(50), "
				+ "social varchar(9));",				

				"create table reimbursements (" 
				+ "id serial primary key, " 
				+ "user_id serial references employees(id), "
				+ "state varchar(20),"
				+ "primary key(id, user_i));",
				// Add byte array image
				
				"create table reimbursement_items (" 
				+ "id serial primary key, " 
				+ "req_id serial references reimbursements(id), "
				+ "item_name varchar(50) not NULL,"
				+ "item_price real not NULL,"
				+ "primary key(id, req_id));"
				};
		try (Statement statement = connection.createStatement()) {
			for (String drop : dropStatements) {
				statement.executeUpdate(drop);
			}
			for (String create : createStatements) {
				statement.executeUpdate(create);
			}
			System.err.println("Created databases");
		} 
		catch (Exception e) {
			System.err.println("Problem recreating databases");
			throw e;
		}		
	}
	
	@GET
	@Path("/fill")
	public void fillDatabase() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("fillDatabase not written");
	}
	
    public ConnectionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
