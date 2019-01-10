package com.revature.services;

import java.util.List;

import com.revature.model.Fanfic;
import com.revature.repository.FanficRepo;

public class FanficService {
	List<Fanfic> fanficList = FanficRepo.getFanfics();
	
	public List<Fanfic> getAllFanfic() {
		return fanficList;
	}
	
	public static void insertFanfic(Fanfic newFanfic) {
		FanficRepo.insertFanfic(newFanfic);
	}
	
}
