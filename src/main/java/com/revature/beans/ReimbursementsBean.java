package com.revature.beans;

import java.io.Serializable;
import java.util.List;

import com.revature.model.Reimbursement;

public class ReimbursementsBean implements Serializable {
	private static final long serialVersionUID = 10242156897977220L;
	
	private List<Reimbursement> pendingReimbursements;
	private List<ResolvedPairBean> resolvedReimbursements;
	
	public ReimbursementsBean() {}
	public ReimbursementsBean(List<Reimbursement> pendingReimbursements, List<ResolvedPairBean> resolvedReimbursements) {
		super();
		this.pendingReimbursements = pendingReimbursements;		
		this.resolvedReimbursements = resolvedReimbursements;
		
	}
	public List<Reimbursement> getPendingReimbursements() {
		return pendingReimbursements;
	}
	public List<ResolvedPairBean> getResolvedReimbursements() {
		return resolvedReimbursements;
	}
}
