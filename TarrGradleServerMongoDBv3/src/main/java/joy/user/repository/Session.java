package joy.user.repository;

import java.util.Vector;

import org.springframework.data.annotation.Id;

import com.google.common.base.Objects;

/**
 *Class to represent a session in DB
 *
 *@author joy
 *
 */

public class Session {

	@Id
	private String sessionId;
	private String str;
	private String requestBy;
	
	private String requestTo;
	private Vector<String> topic;
	private String sessionTime;
	private String sessionDuration;
	private String sessionLocation;
	private float sessionRating;
	private String remarks;
	private Boolean isConfirmed;
	private Boolean isCompleted;
	private Boolean isDenied;
	
	
	public Session(String sessionId, String str,String requestBy, String requestTo,
	Vector<String> topic, String sessionTime, String sessionDuration,
	String sessionLocation, float sessionRating, String remarks,Boolean isConfirmed, Boolean isCompleted, Boolean isDenied)
	
	{
		super();
		this.sessionId = sessionId;
		this.str = str;
		this.requestBy = requestBy;
		this.requestTo = requestTo;
		this.topic = topic;
		this.sessionTime = sessionTime;
		this.sessionDuration = sessionDuration;
		this.sessionLocation = sessionLocation;
		this.sessionRating = sessionRating;
		this.remarks = remarks;
		this.isCompleted = isCompleted;
		this.isConfirmed = isConfirmed;
		this.isDenied = isDenied;
		
		
	}

	public Session(){
		//dummy constructor for Jackson 
	}
	
	
	public Boolean getIsCompleted(){
		return this.isCompleted;
	}
	
	public void setIsCompleted(Boolean temp){
		this.isCompleted = temp;		
	}
	public Boolean getIsConfirmed(){
		return this.isConfirmed;
	}
	
	public void setIsConfirmed(Boolean temp){
		this.isConfirmed = temp;		
	}
	
	
	public Boolean getIsDenied(){
		return this.isDenied;
	}
	
	public void setIsDenied(Boolean temp){
		this.isDenied = temp;		
	}
	
	
	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String temp){
		this.sessionId = temp;		
	}
	public String getStr(){
		return this.str;
	}
	
	public void setStr(String temp){
		this.str = temp;		
	}
	
	public String getRequestBy(){
		return this.requestBy;
	}
	
	public void setRequestBy(String temp){
		this.requestBy = temp;		
	}
	
	public String getRequestTo(){
		return this.requestTo;
	}
	
	public void setRequestTo(String temp){
		this.requestTo = temp;		
	}
	
	public Vector<String> getTopic(){
		return this.topic;
	}
	
	public void setTopic(Vector<String> temp){
		this.topic = temp;		
	}
	
	public String getSessionTime(){
		return this.sessionTime;
	}
	
	public void setSessionTime(String temp){
		this.sessionTime = temp;		
	}
	
	public String getSessionDuration(){
		return this.sessionDuration;
	}
	
	public void setSessionDuration(String temp){
		this.sessionDuration = temp;		
	}
	
	public String getSessionLocation(){
		return this.sessionLocation;
	}
	
	public void setSessionLocation(String temp){
		this.sessionLocation = temp;		
	}
	
	public float getSessionRating(){
		return this.sessionRating;
	}
	
	public void setSessionRating(float temp){
		this.sessionRating = temp;		
	}
	
	public String getRemarks(){
		return this.remarks;
	}
	
	public void setRemarks(String temp){
		this.remarks = temp;		
	}
	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(sessionId,requestBy,requestTo);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Session) {
			Session other = (Session) obj;
			// Google Guava provides great utilities for equals too!
			return ( Objects.equal(requestTo, other.requestTo) 
					&& Objects.equal(requestBy, other.requestBy));
					} else {
			return false;
		}
	}



}
