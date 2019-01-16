package com.revature.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
	private String item_name;
	private Double item_price;
	
	public Item(ResultSet rs) throws SQLException {
		this.item_name = rs.getString("item_name");
		this.item_price = rs.getDouble("item_price");
	}
	
	public String getItem_name() {
		return item_name;
	}
	public Double getItem_price() {
		return item_price;
	}
}
