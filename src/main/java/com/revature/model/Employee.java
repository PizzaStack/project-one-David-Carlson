package com.revature.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee implements Serializable {
	private static final long serialVersionUID = 6297385302078200511L;
	
	private int id;
	private Boolean is_manager;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	public Employee(int id, Boolean isManager, String username, String password, String firstName, String lastName) {
		super();
		this.id = id;
		this.is_manager = isManager;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public Employee(Boolean isManager, String username, String password, String firstName, String lastName) {
		this(-1, isManager, username, password, firstName, lastName);
	}
	public Employee(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.is_manager = rs.getBoolean("is_manager");
		this.username = rs.getString("username");
		this.password = rs.getString("password");
		this.firstName = rs.getString("firstName");
		this.lastName = rs.getString("lastName");
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
