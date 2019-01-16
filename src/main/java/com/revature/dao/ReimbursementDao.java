package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Employee;
import com.revature.model.Item;
import com.revature.model.Reimbursement;
import com.revature.servlets.ConnectionServlet;

public class ReimbursementDao {
	
	public static Boolean addReimbursement(Reimbursement reim) {
		Connection conn = ConnectionServlet.getConnection();
		
		String sql = "insert into reimbursements (user_id, state) values (?,?);";
		try (PreparedStatement statement = conn.prepareStatement(sql, 
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, reim.getUser_id());
			statement.setString(2, reim.getState());			
			
			long new_req_id = -1l;
			conn.setAutoCommit(false);
			if (statement.executeUpdate() == 1) {
				try (ResultSet rs = statement.getGeneratedKeys()) {
					if (rs.next()) {
						new_req_id = rs.getLong(1);
						if (addReimbursementItems(conn, new_req_id, reim.getItems())) {
							conn.commit();
							return true;
						}							
					}
				}
			}
			conn.rollback();
		} catch (SQLException e) {
			try { conn.rollback();} catch (SQLException e1) {}
			e.printStackTrace();
		}
		finally {
			try {
				conn.setAutoCommit(true);
			}
			catch(SQLException e) {System.out.println("Somehow didn't turn on auto-commit");}
		}
		return false;		
	}
	
	public static Boolean addReimbursementItems(Connection conn, long req_id, List<Item> items) {
		for (Item item : items) {
			String sql = "insert into reimbursement_items (req_id, item_name, item_price) values (?,?,?);";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setInt(1, (int) req_id);
				statement.setString(2, item.getItem_name());
				statement.setDouble(3, item.getItem_price());
				if (statement.executeUpdate() != 1)
					return false;
			}
			catch(SQLException e) {
				System.out.println("Error adding reim item: " + e.toString());
				return false;
			}
		}
		return true;		
	}
	
	public static List<Item> getReimbursementItems(int req_id) throws SQLException{
		List<Item> items = new ArrayList<Item>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursement_items where req_id=?";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					items.add(new Item(resultSet));					
				}			
			}
		}
		
		return items;
	}
	
	public static List<Reimbursement> getEmployeePendingReimbursements(int employee_id) {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where user_id=? and state='Pending';";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployeePendingReims:" + e.toString());
			return null;
		}
		return reimbursements;
	}
	public static List<Reimbursement> getEmployeeResolvedReimbursements(int employee_id) {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where user_id=? and state<>'Pending';";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployeePendingReims:" + e.toString());
			return null;
		}
		return reimbursements;
	}
	public static List<Reimbursement> getPendingReimbursementsAsManager() {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where state='Pending';";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployeePendingReims:" + e.toString());
			return null;
		}
		return reimbursements;
	}
	
}
