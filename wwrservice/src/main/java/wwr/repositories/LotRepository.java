package wwr.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Lot;
import wwr.model.enities.Person;
import wwr.model.enities.Recipe;

public interface LotRepository extends CrudRepository<Lot,Long>{
	public Iterable<Lot> findAll() ;
	public Iterable<Lot> findByRecipe( Recipe recipe ) ;
	public Iterable<Lot> findByRecipeOwner( Person person ) ;

	public Optional<Lot> findById(Long id) ;	
}

	