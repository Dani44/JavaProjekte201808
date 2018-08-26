package wwr.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Person;

@Transactional
public interface PersonRepository extends CrudRepository<Person , Long>{

	public Iterable<Person> findAll() ;
	public Person findByNickname( String nickname );
	public Person findByEmail( String email );
	public Optional<Person> findById(Long id) ;

}
