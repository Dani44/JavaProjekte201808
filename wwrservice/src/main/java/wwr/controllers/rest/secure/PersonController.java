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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wwr.model.enities.Person;
import wwr.repositories.PersonRepository;

@RestController
public class PersonController {
	
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(PersonController.class);


	  @Autowired
	  private PersonRepository personRepository;

	
	
	 @RequestMapping(value = "/rest/secure/persons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Iterable<Person> getAllPersons() {
		 Iterable<Person> personlist = personRepository.findAll();
		 return personlist ;
	 }
	 
	 @CrossOrigin
	 @Secured({"USER","ADMIN"})
	 @RequestMapping(value = "/rest/secure/persons/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Person getMe() {
		 Person person = null ;
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 person = personRepository.findByNickname(auth.getName());
		 return person ;
	 }

	 @RequestMapping(value = "/rest/secure/persons/{idornickname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Person getPersonByIdOrNickname( @PathVariable String idornickname ) {
		 Person person = null ;
		 if( idornickname != null){
			 try {
				 Long id = Long.parseLong(idornickname) ;
				 person = personRepository.findById(id).get();
				
			 } catch (NumberFormatException e) {
				 person = personRepository.findByNickname(idornickname);
			 }
		 }
		 return person ;
	 }


	 @RequestMapping(value = "/rest/secure/persons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Person> savePerson( @RequestBody Person person ){
		Person personNew = null ;
  	    personNew = personRepository.save(person) ;
		return ResponseEntity.ok(personNew);
	 }
	 

	 @RequestMapping(value = "/rest/secure/persons", method = RequestMethod.DELETE)
	 public ResponseEntity<String> deletePerson( @RequestParam Long id) throws IOException {
			ResponseEntity<String> response = null;
			response = new ResponseEntity<String>(HttpStatus.OK);
		    personRepository.deleteById(id) ;
		    return response ;
	 }
	 
} 
