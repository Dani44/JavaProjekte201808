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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wwr.model.enities.Person;
import wwr.model.enities.Recipe;
import wwr.model.enities.Review;
import wwr.model.enities.ReviewAttachment;
import wwr.repositories.LotRepository;
import wwr.repositories.PersonRepository;
import wwr.repositories.RecipeRepository;


@RestController
public class RecipeController {
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private LotRepository  lotRepository;


	@RequestMapping(value = "/rest/secure/myrecipies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Recipe> getMyRecipies( ) {
		Iterable<Recipe> recipelist = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Person pSearch = personRepository.findByNickname(auth.getName()) ;
		recipelist = recipeRepository.findByOwner(pSearch);
	    return recipelist ;
    }
	
	@RequestMapping(value = "/rest/secure/myrecipies", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> saveMyRecipe( @RequestBody Recipe recipe){
	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	   Person pSearch = personRepository.findByNickname(auth.getName()) ;
	   Recipe recipeNew = null ;
	   recipe.setOwner(pSearch);

	   // ---------------------------------------------------------------------------
	   // Manually Cascade of Attatchment 
	   // --------------------------------------------------------------------------
	   for( Review r : recipe.getReviews()){
		   if(r.getAttachments() != null ){
			   for( ReviewAttachment ra : r.getAttachments()){
				   ra.setReview(r);
			   }		   			   
		   }
	   }
	   
	   

	   recipeNew = recipeRepository.save(recipe) ;
	   
	   return ResponseEntity.ok(recipeNew);
	}

	@RequestMapping(value = "/rest/secure/myrecipies", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMyRecipe( @RequestParam Long id) throws IOException {
		recipeRepository.deleteById(id) ;
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
		return response ;	
	}
	
	
	
	
	@RequestMapping(value = "/rest/secure/{nickname}/recipies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Recipe getRecipeById(@PathVariable  String nickname ,  @PathVariable Long id ) {
	   Recipe recipe = recipeRepository.findById(id).get() ;
	   return recipe ;
	}
	 
	 
	@RequestMapping(value = "/rest/secure/recipies", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteRecipe( @PathVariable  String nickname, @RequestParam Long id) throws IOException {
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(HttpStatus.OK);
		recipeRepository.deleteById(id) ;
	    return response ;
	}

	@CrossOrigin
	@Secured({"USER","ADMIN"})
	@RequestMapping(value = "/rest/secure/recipies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Recipe> getAllRecipies( ) {
		Iterable<Recipe> recipelist = null;
		recipelist = recipeRepository.findAll();			   
	    return recipelist ;
    }

	
	@RequestMapping(value = "/rest/secure/{nickname}/recipies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Recipe> getAllUserRecipies( @PathVariable  String nickname ) {
		
		Iterable<Recipe> recipelist = null;
		Person p = personRepository.findByNickname(nickname) ;
		recipelist = recipeRepository.findByOwner(p);
	   
	   return recipelist ;
    }

}
