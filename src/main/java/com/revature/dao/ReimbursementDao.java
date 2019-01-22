

package com.revature.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.revature.beans.ReimbursementsBean;
import com.revature.beans.ResolvedPairBean;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.servlets.ConnectionServlet;

public class ReimbursementDao {
	
	public static Boolean addReimbursement(Reimbursement reim) {
		Connection conn = ConnectionServlet.getConnection();
		Boolean hasResolver = reim.getResolved_by() > 0;
		String sql = "insert into reimbursements (user_id, state, item_name, item_price, resolved_by, receipt_image) values (?,?,?,?,?,?);";

		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, reim.getUser_id());
			statement.setString(2, reim.getState());	
			statement.setString(3, reim.getItem_name());
			statement.setDouble(4, reim.getItem_price());
			statement.setInt(5, reim.getResolved_by());
			statement.setBytes(6, reim.getReceipt_image());
	
			if (statement.executeUpdate() == 1) 
				return true;
			
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

	public static List<Reimbursement> getEmployeePendingReimbursements(int employee_id) {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where user_id=? and state='Pending';";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, employee_id);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployeePendingReims:" + e.toString());
			return Reimbursement.Not_Found_List;
		}
		return reimbursements;
	}
	public static List<Reimbursement> getEmployeeResolvedReimbursements(int employee_id) {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where user_id=? and (state=? or state=?);";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, employee_id);
			statement.setString(2, Reimbursement.Approved);
			statement.setString(3, Reimbursement.Denied);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getEmployeeResolvedReims:" + e.toString());
			return Reimbursement.Not_Found_List;
		}
		return reimbursements;
	}
	
	public static List<ResolvedPairBean> getResolversForReimbursements(List<Reimbursement> reims) {
		Map<Integer,Employee> employees = new HashMap<Integer, Employee>();
		List<ResolvedPairBean> reimBean = new ArrayList<ResolvedPairBean>();
		for (Employee e : EmployeeDao.getAllManagers()) {
//			System.out.println("Adding e " + e.toString());
			employees.put(e.getId(), e);
		}
//		for (Integer key : employees.keySet()) {
//			System.out.println(String.format("%s-%s", key, employees.get(key)));
//		}
//		List<ResolvedPairBean> pairs = new ArrayList<ResolvedPairBean>();
//		for (Reimbursement reim : reims) {
//			System.out.println(String.format("%s-reim + %s", reim.toString(), reim.getResolved_by()));
//		}
//		return pairs;
		return reims.stream().map(r -> new ResolvedPairBean(r, employees.get(r.getResolved_by()))).collect(Collectors.toList());
	}

	public static List<Reimbursement> getAllPendingReimbursementsAsManager() {
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
			System.out.println("SQLError in getAllPendingReims:" + e.toString());
			return Reimbursement.Not_Found_List;
		}
		return reimbursements;
	}
	public static List<Reimbursement> getAllResolvedReimbursementsAsManager() {
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		Connection conn = ConnectionServlet.getConnection();
		String sql = "select * from reimbursements where state<>'Pending';";
		
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					reimbursements.add(new Reimbursement(resultSet));					
				}			
			}
		}
		catch (SQLException e) {
			System.out.println("SQLError in getAllResolvedreims:" + e.toString());
			return Reimbursement.Not_Found_List;
		}
		return reimbursements;
	}
	
	public static Boolean resolveReimbursement(int req_id, String new_state, int manager_id) {
		if (!new_state.equals(Reimbursement.Approved) && !new_state.equals(Reimbursement.Denied)) {
			System.out.println("Given invalid resolved state: " + new_state);
			return false;
		}
		Connection conn = ConnectionServlet.getConnection();
		String sql = "update reimbursements set state=?, resolved_by=? where id=? and state=?;";
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, new_state);
			statement.setInt(2, manager_id);
			statement.setInt(3, req_id);
			statement.setString(4, Reimbursement.Pending);
			if (statement.executeUpdate() == 1)
				return true;
		} catch (SQLException e) {
			System.out.println("Error updating employee: " + e.toString());		
		}
		return false;
	}
	
}
