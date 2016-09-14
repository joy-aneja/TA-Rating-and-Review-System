package joy.user.repository;

import java.util.Collection;
import java.util.List;

import joy.user.client.UserClientApi;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;


@RepositoryRestResource(path = "/user")
public interface UserRepository extends MongoRepository<User, String>{

	
	public Collection<User> findByEmailIdIgnoreCase(
			@Param ("emailid") String emailId);
	
	public Collection<User> findBySkillsContainingIgnoreCase(
			@Param ("skill") String skill);
			
	public Collection<User> findByNameContainingIgnoreCase(
			@Param ("name") String name);
	
	public Collection<User> findByCourseContainingIgnoreCase(
			@Param ("course") String course);
	/*
	public long deleteByEmailId(
			@Param ("emailid") String emailId);
	
	*/
}
