package com.example.joy.tarr;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**Interface for strong typing on both client and server interface
*
*@author joy
*/

public interface ProfessorClientApi {

	
	@GET("/professor")
	public Collection<Professor> getProfessorList();
	
	@POST("/professor")
	public Boolean addProfessor(@Body Professor prof);
	
	@GET("/professor/search/findByEmailIdIgnoreCase")
	public Collection<Professor> findByEmailIdIgnoreCase(@Query("emailid") String emailId);


}
