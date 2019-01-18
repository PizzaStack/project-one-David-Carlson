package com.revature.beans;

import java.io.Serializable;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;

public class ResolvedPairBean implements Serializable {
	private static final long serialVersionUID = -6104565135292665450L;
	
	private Reimbursement reimbursement;
	private Employee resolver;
	
	public ResolvedPairBean() {
		super();
	}
	public ResolvedPairBean(Reimbursement reimbursement, Employee resolver) {
		super();
		this.reimbursement = reimbursement;
		this.resolver = resolver;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Reimbursement getReimbursement() {
		return reimbursement;
	}
	public Employee getResolver() {
		return resolver;
	}
	public void setEmployee(Employee resolver) {
		this.resolver = resolver;
	}
	
	
	
}
