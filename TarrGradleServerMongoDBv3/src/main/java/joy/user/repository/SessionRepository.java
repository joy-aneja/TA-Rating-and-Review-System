package joy.user.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/session")
public interface SessionRepository extends MongoRepository<Session,String>{

	
	public Collection<Session> findByRequestByIgnoreCase(
			@Param ("requestby") String requestby);
	
	public Collection<Session> findByRequestToIgnoreCase(
			@Param ("requestto") String requestto);
			
	public Collection<Session> findByTopicContainingIgnoreCase(
			@Param ("topic") String topic);
	
	/*public Collection<User> findByCourseContaining(
			@Param ("course") String course);*/
	
}
