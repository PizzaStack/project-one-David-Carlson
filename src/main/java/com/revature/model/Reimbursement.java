package com.revature.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.ReimbursementDao;

public class Reimbursement {
	private int id;
	private int user_id;
	private String state;
	private String item_name;
	private Double item_price;
	private int resolved_by;
	private byte[] receipt_image;
	
	public transient final static Reimbursement NOT_FOUND = new Reimbursement(-1, -1, "No state", "No item", -1.0d, 0, null);
	public transient final static List<Reimbursement> Not_Found_List = new ArrayList<Reimbursement>();
	
	public static String Pending = "Pending";
	public static String Approved = "Approved";
	public static String Denied = "Denied";
	
	public Reimbursement(int id, int user_id, String state, String item_name, Double item_price, int resolved_by, byte[] receipt_image) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.state = state;
		this.item_name = item_name;
		this.item_price = item_price;
		this.resolved_by = resolved_by;
		this.receipt_image = receipt_image;
	}
	public Reimbursement(int user_id, String state, String item_name, Double item_price, int resolved_by, byte[] receipt_image) {
		this(0, user_id, state, item_name, item_price, resolved_by, receipt_image);
	}

	public Reimbursement(ResultSet rs) throws SQLException {
		id = rs.getInt("id");
		user_id = rs.getInt("user_id");
		state = rs.getString("state");		
		item_name = rs.getString("item_name");
		item_price = rs.getDouble("item_price");
		resolved_by = rs.getInt("resolved_by");
		resolved_by = resolved_by < 1? 0 : resolved_by;
		receipt_image = rs.getBytes("receipt_image");
	}

	public byte[] getReceipt_image() {
		return receipt_image;
	}
	public static Reimbursement getNotFound() {
		return NOT_FOUND;
	}
	public static List<Reimbursement> getNotFoundList() {
		return Not_Found_List;
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

	public String getItem_name() {
		return item_name;
	}

	public Double getItem_price() {
		return item_price;
	}
	public int getResolved_by() {
		return resolved_by;
	}
	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", user_id=" + user_id + ", state=" + state + ", item_name=" + item_name
				+ ", item_price=" + item_price + ", resolved_by=" + resolved_by + "]";
	}
}
