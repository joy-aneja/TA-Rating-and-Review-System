package joy.user.client;

import java.util.Collection;

import joy.user.repository.Professor;
import joy.user.repository.Session;
import joy.user.repository.User;
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
	public Collection<User> findBySkillsContainingIgnoreCase(@Query("skill")String skill);
	
	@GET("/user/search/findByNameContainingIgnoreCase")
	public Collection<User> findByNameContainingIgnoreCase(@Query("name") String name);
	
	@GET("/user/search/findByCourseContainingIgnoreCase")
	public Collection<User> findByCourseContainingIgnoreCase(@Query("course") String course);
	
	//@GET("/user/)
	
}
