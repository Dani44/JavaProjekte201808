package wwr.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import wwr.model.enities.Person;
import wwr.model.enities.Recipe;
import wwr.model.enities.Review;

public interface ReviewRepository extends CrudRepository<Review,Long>{
	public Iterable<Review> findAll() ;
	public Iterable<Review> findByRecipe( Recipe recipe ) ;
	public Iterable<Review> findByRecipeOwner( Person person ) ;

	public Optional<Review> findById(Long id) ;	
}

	