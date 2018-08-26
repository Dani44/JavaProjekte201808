package wwr.controllers.rest.secure;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wwr.model.enities.Person;
import wwr.model.enities.Recipe;
import wwr.model.enities.Review;
import wwr.repositories.PersonRepository;
import wwr.repositories.RecipeRepository;
import wwr.repositories.ReviewRepository;

@RestController
public class ReviewController {
	
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private ReviewRepository reviewRepository;	
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	
	@RequestMapping(value = "/rest/secure/myreviews", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Review> getMyReviews( ) {
		Iterable<Review> reviewlist = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Person pSearch = personRepository.findByNickname(auth.getName()) ;
		reviewlist = reviewRepository.findByRecipeOwner(pSearch);
	    return reviewlist ;
    }	
	
	@RequestMapping(value = "/rest/secure/myreviews", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Review> saveMyReview( @RequestBody Review review){
	   Review reviewNew = null ;
	   reviewNew = reviewRepository.save(review) ;
	   return ResponseEntity.ok(reviewNew);
	}

	@Secured({"USER","ADMIN"})
	@RequestMapping(value = "/rest/secure/myreviews", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMyReview( @RequestParam Long id) throws IOException {
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(HttpStatus.OK);
		reviewRepository.deleteById(id) ;
	    return response ;		    
	}	
	
	
	
	@RequestMapping(value = "/rest/secure/recipies/{recipeId}/reviews", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Review> getRecipeReviews( @PathVariable("recipeId") Long recipeId ) {
		Iterable<Review> reviewlist = null;
		Recipe recipe = recipeRepository.findById(recipeId).get() ;		
		reviewlist = reviewRepository.findByRecipe(recipe);
	    return reviewlist ;
    }	
	
	
	
	
	
	
}
