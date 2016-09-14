package org.magnum.mobilecloud.video;

import java.util.UUID;
import java.util.Vector;

import joy.user.repository.User;

import com.fasterxml.jackson.databind.ObjectMapper;



public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static User randomUser() {
		
		
		String id = UUID.randomUUID().toString();
		Vector<String> skills = new Vector<String>();
		skills.add("JAVA Programming");
		Vector<Vector<String>> ratingHis= new Vector<Vector<String>>();
		Vector<String> temp = new Vector<String>();
		temp.add("sid");
		temp.add("5.0");
		ratingHis.add(temp);
		
		
		return new User(id,id,"",
				"himanshuvar@gmail.com",
				"himanshu varshney",
				"MTech Ist year","CSE",
				"IIIT Delhi",skills,
				(float) 0.0,"Pushpendra",
				"PCSMA",0,0,0,0,ratingHis
				) ;
	}
	
	
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
