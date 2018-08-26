package wwr.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import wwr.model.enities.Person;
import wwr.model.enities.UserDocument;

public interface UserDocumentRepository extends CrudRepository<UserDocument,Long>{
	public Iterable<UserDocument> findAll() ;
	public Iterable<UserDocument> findByOwner( Person person ) ;
	public Optional<UserDocument> findById(Long id) ;	
}

	