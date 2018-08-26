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

import wwr.model.enities.Lot;
import wwr.model.enities.Person;
import wwr.model.enities.Recipe;
import wwr.repositories.LotRepository;
import wwr.repositories.PersonRepository;
import wwr.repositories.RecipeRepository;

@RestController
public class LotController {
	
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LotController.class);

	@Autowired
	private LotRepository lotRepository;	
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	
	@RequestMapping(value = "/rest/secure/mylots", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Lot> getMyLots( ) {
		Iterable<Lot> lotlist = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Person pSearch = personRepository.findByNickname(auth.getName()) ;
		lotlist = lotRepository.findByRecipeOwner(pSearch);
	    return lotlist ;
    }	
	
	@RequestMapping(value = "/rest/secure/mylots", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Lot> saveMyLot( @RequestBody Lot lot){
	   Lot lotNew = null ;
	   lotNew = lotRepository.save(lot) ;
	   return ResponseEntity.ok(lotNew);
	}

	@Secured({"USER","ADMIN"})
	@RequestMapping(value = "/rest/secure/mylots", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMyLot( @RequestParam Long id) throws IOException {
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(HttpStatus.OK);
		lotRepository.deleteById(id) ;
	    return response ;		    
	}	
	
	
	
	@RequestMapping(value = "/rest/secure/recipies/{recipeId}/lots", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Lot> getRecipeLots( @PathVariable("recipeId") Long recipeId ) {
		Iterable<Lot> lotlist = null;
		Recipe recipe = recipeRepository.findById(recipeId).get() ;		
		lotlist = lotRepository.findByRecipe(recipe);
	    return lotlist ;
    }	
	
	
	
	
	
	
}
