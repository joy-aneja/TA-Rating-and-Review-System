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


public interface UserClientApi {
	
	@GET("/user")
	public Collection<User> getUserList();
	
	@POST("/user")
	public Boolean addUser(@Body User user);
	
	@GET("/user/search/findByEmailIdIgnoreCase")
	public Collection<User> findByEmailIdIgnoreCase(@Query("emailid") String emailId);

	@GET("/user/search/findBySkillsContainingIgnoreCase")
	public Collection<User> findBySkillsContainingIgnoreCase(@Query("skill") String skill);
	
	@GET("/user/search/findByNameContainingIgnoreCase")
	public Collection<User> findByNameContainingIgnoreCase(@Query("name") String name);
	
	@GET("/user/search/findByCourseContainingIgnoreCase")
	public Collection<User> findByCourseContainingIgnoreCase(@Query("course") String course);

    @POST("/sendGcm")
    public Boolean sendGcm(@Body String regid, @Query("title") String title, @Query("data") String message);


}
