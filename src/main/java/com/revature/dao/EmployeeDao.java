package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Employee;
import com.revature.servlets.ConnectionServlet;

public class EmployeeDao {
	public static List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from employees where is_manager is FALSE;";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					employees.add(new Employee(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployee" + e.toString());
			return null;
		}
		return employees;
	}
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
	
	public static Boolean updateEmployee(Employee employee) {
		Connection conn = ConnectionServlet.getConnection();
		String sql = "update employees set username=? and password=? and firstname=? and lastname=? where id=?";
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, employee.getUsername());
			statement.setString(2, employee.getPassword());
			statement.setString(3, employee.getFirstName());
			statement.setString(4, employee.getLastName());
			statement.setInt(5, employee.getId());
			if (statement.executeUpdate() == 1)
				return true;
		} catch (SQLException e) {
			System.out.println("Error updating employee: " + e.toString());		
		}
		return false;
	}
	public static Boolean updateEmployeeAsManager(Employee employee) {
		Connection conn = ConnectionServlet.getConnection();
		String sql = "update employees set is_manager=? and username=? and password=? and firstname=? and lastname=? where id=?";
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setBoolean(1, employee.getIsManager());
			statement.setString(2, employee.getUsername());
			statement.setString(3, employee.getPassword());
			statement.setString(4, employee.getFirstName());
			statement.setString(5, employee.getLastName());
			statement.setInt(6, employee.getId());
			if (statement.executeUpdate() == 1)
				return true;
		} catch (SQLException e) {
			System.out.println("Error updating employee as manager: " + e.toString());		
		}
		return false;
	}
	
	public static Boolean addEmployee(Employee employee) {
		Connection conn = ConnectionServlet.getConnection();
		String sql = "insert into employees (is_manager, username, password, firstname, lastname) "
				+ "values (?, ?, ?, ?, ?);";
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setBoolean(1, employee.getIsManager());
			statement.setString(2, employee.getUsername());
			statement.setString(3, employee.getPassword());
			statement.setString(4, employee.getFirstName());
			statement.setString(5, employee.getLastName());
			
			if (statement.executeUpdate() == 1)
				return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	

	
	
}
