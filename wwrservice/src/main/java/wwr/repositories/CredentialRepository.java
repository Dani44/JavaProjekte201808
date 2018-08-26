package wwr.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Credential;
import wwr.model.enities.Person;

@Transactional
public interface CredentialRepository extends CrudRepository<Credential, Long>{
	public Iterable<Credential> findAll() ;

}
