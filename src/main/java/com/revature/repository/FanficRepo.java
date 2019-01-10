package com.revature.repository;

import java.util.ArrayList;
import java.util.List;

import com.revature.model.Fanfic;

public class FanficRepo {
	
	private static List<Fanfic> fanficList = new ArrayList<Fanfic>();
	
	static {
		fanficList.add(new Fanfic(1, "Darth Jar Jar", "A long time ago..."));
		fanficList.add(new Fanfic(2, "Jabba the slug", "Yep, it happened"));
	}
	
	public static List<Fanfic> getFanfics() {
		return fanficList;
	}
	
	public static void insertFanfic(Fanfic newFanfic) {
		fanficList.add(newFanfic);
	}

}
