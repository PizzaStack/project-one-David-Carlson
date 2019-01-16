package com.revature.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.revature.dao.ReimbursementDao;

public class Reimbursement {
	private int id;
	private int user_id;
	private String state;
	private List<Item> items;
	private Integer resolving_manager;
	public enum States {
		Pending,
		Approved,
		Denied
	}
	
	public Reimbursement(ResultSet rs) throws SQLException {
		id = rs.getInt("id");
		user_id = rs.getInt("user_id");
		state = rs.getString("state");		
		items = ReimbursementDao.getReimbursementItems(id);
		resolving_manager = rs.getInt("resolved_by");
		if (rs.wasNull())
			resolving_manager = null;
	}	

	public int getId() {
		return id;
	}

	public int getUser_id() {
		return user_id;
	}

	public String getState() {
		return state;
	}

	public List<Item> getItems() {
		return items;
	}
}
