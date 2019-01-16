package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.revature.model.Employee;
import com.revature.servlets.ConnectionServlet;

public class EmployeeDao {
	
	public static Employee getEmployee(String username, String password) {
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from employees where username=? AND password=?";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, username);
			statement.setString(2, password);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					Employee employee =  new Employee(resultSet);
					if (resultSet.next()) {
						System.out.println("Multiple users with username/password");
						return null;
					}
					else 
						return employee;
				}					
				else 
					return null;
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployee" + e.toString());
			return null;
		}
	}
}
