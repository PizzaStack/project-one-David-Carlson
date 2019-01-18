package com.revature.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable {
	private static final long serialVersionUID = 6297385302078200511L;
	
	private int id;
	private Boolean is_manager;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	public transient final static Employee NOT_FOUND = new Employee(-1, false, "Not found", "Not found", "Not found", "Not found");
	public transient final static List<Employee> NOT_FOUND_LIST = new ArrayList<Employee>();
	
	public Employee (Integer id, Boolean isManager, String username, String password, String firstName, String lastName)
			throws IllegalArgumentException  {
		super();
		if (id == null || isManager == null || username == null || password == null || firstName == null || lastName == null)
			throw new IllegalArgumentException("Employee Initialized with null value");
		this.id = id;
		this.is_manager = isManager;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public Employee(Boolean isManager, String username, String password, String firstName, String lastName)
	throws IllegalArgumentException {
		this(-1, isManager, username, password, firstName, lastName);
	}
	public Employee(ResultSet rs) throws SQLException, IllegalArgumentException {
		this.id = rs.getInt("id");
		this.is_manager = rs.getBoolean("is_manager");
		this.username = rs.getString("username");
		this.password = rs.getString("password");
		this.firstName = rs.getString("firstName");
		this.lastName = rs.getString("lastName");
		
		if (id == 0 || is_manager == null || username == null || password == null || firstName == null || lastName == null)
			throw new IllegalArgumentException("Employee Initialized with null value");
	}
	
	@Override
	public String toString() {
		return String.format("Username: %s, password: %s, Manager?: %s", 
				this.username, this.password, this.is_manager? "Yes": "No");
	}
    /* Getters and setters */
	public int getId() {
		return id;
	}
	public Boolean getIsManager() {
		return is_manager;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}	
}
