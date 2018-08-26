package wwr.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Person;
import wwr.model.enities.Recipe;

@Transactional
public interface RecipeRepository extends CrudRepository<Recipe, Long>{
	public Iterable<Recipe> findAll() ;
	public Iterable<Recipe> findByOwner( Person person ) ;
	public Optional<Recipe> findById(Long id) ;
	
	// Sample ....
	// @Query("from Auction a join a.category c where c.name=:categoryName")
	// public Iterable<Auction> findByCategory(@Param("categoryName") String categoryName);
	
	
}
