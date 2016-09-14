package joy.user.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/professor")
public interface ProfessorRepository extends MongoRepository<Professor, String>{
	
	public Collection<Professor> findByEmailIdIgnoreCase(
			@Param ("emailid") String emailId);
}
