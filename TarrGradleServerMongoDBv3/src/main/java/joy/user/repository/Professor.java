package joy.user.repository;

import java.util.Vector;

import org.springframework.data.annotation.Id;

import com.google.common.base.Objects;

/**
 *Class to represent a professor in DB
 *
 *@author joy
 *
 */

public class Professor {
	@Id
	private String id;
	private String emailId;
	private String name;
	private String department;
	private Vector<String> courses;
	private Vector<Vector<String>> ta;
	
	
	public Professor (String id, String emailId ,String name, String department, 
			Vector<String> courses, Vector<Vector<String>> ta){
		super();
		this.id=id;
		this.emailId=emailId;
		
		this.name = name;
		this.department = department;
		this.courses = courses;
		this.ta = ta;
	}
	
	public Professor(){
		//dummy constructor for JACKSON
	}
	
	public String getId(){
		return this.id;
	
	}
	public void setId(String temp){
		this.id = temp;
	}
	
	public String getEmailId(){
		return this.emailId;
	}
	
	public void setEmailId(String temp){
		this.emailId = temp;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String temp){
		this.name = temp;
	}
	
	public String getDepartment(){
		return this.department;
	}
	
	public void setDepartment(String temp){
		this.department = temp;
	}
	
	public Vector<String> getCourses(){
		return this.courses;
	}
	
	public void setTa(Vector<Vector<String>> temp){
		this.ta = temp;
	}
	
	public Vector<Vector<String>> getTa(){
		return this.ta;
	}
	
	public void setCourses(Vector<String> temp){
		this.courses = temp;
	}
	
	

	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(emailId,name);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Professor) {
			Professor other = (Professor) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(emailId, other.emailId);
					
		} else {
			return false;
		}
	}
	
}
