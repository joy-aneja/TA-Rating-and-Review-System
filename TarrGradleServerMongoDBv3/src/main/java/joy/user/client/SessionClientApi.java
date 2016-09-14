package joy.user.client;

import java.util.Collection;

import joy.user.repository.Session;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**Interface for strong typing on both client and server interface
*
*@author joy
*/

public interface SessionClientApi {

	
	

	@GET("/session")
	public Collection<Session> getSessionList();
	
	@POST("/session")
	public Boolean addSession(@Body Session session);
	
	@GET("/session/search/findByRequestByIgnoreCase")
	public Collection<Session> findByRequestByIgnoreCase(@Query("requestby") String requestby);
	
	@GET("/session/search/findByRequestToIgnoreCase")
	public Collection<Session> findByRequestToIgnoreCase(@Query("requestto") String requestto);
	
	@GET("/session/search/findByTopicContainingIgnoreCase")
	public Collection<Session> findByTopicContainingIgnoreCase(@Query("topic") String topic);


}
