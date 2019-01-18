package com.revature.beans;

import java.io.Serializable;

public class LoginDataBean implements Serializable {
	private static final long serialVersionUID = 6297385302078200511L;
	
	public String username;
	public String password;
	
	public LoginDataBean() {}
	public LoginDataBean(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	

}
