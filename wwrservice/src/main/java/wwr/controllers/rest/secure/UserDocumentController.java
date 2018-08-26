package wwr.controllers.rest.secure;


import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import wwr.model.enities.Person;
import wwr.model.enities.ReviewAttachment;
import wwr.model.enities.UserDocument;
import wwr.repositories.PersonRepository;
import wwr.repositories.UserDocumentRepository;

@RestController
public class UserDocumentController {
	
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserDocumentController.class);


	@Autowired
	private UserDocumentRepository udoRepository;	
	
	@Autowired
	private PersonRepository personRepository;


	
	@RequestMapping(value = "/rest/secure/myfiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<UserDocument> getMyUserDocuments( ) {
		Iterable<UserDocument> udolist = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Person pSearch = personRepository.findByNickname(auth.getName()) ;
		udolist = udoRepository.findByOwner(pSearch);
	    return udolist ;
    }	
	
	@RequestMapping(value = "/rest/secure/myfiles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDocument> saveMyUserDocument( @RequestBody UserDocument udo ){
	   UserDocument udoNew = null ;
	   udoNew = udoRepository.save(udoNew) ;
	   return ResponseEntity.ok(udoNew);
	}


	@RequestMapping(value = "/rest/secure/myfiles", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteMyUserDocument(  @RequestParam Long id ){
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(HttpStatus.OK);
		
		UserDocument udo = udoRepository.findById(id).get() ;
	
		try {
			UserDocFileIoController.deleteFile(udo);
			udoRepository.delete(udo);
		} catch (IOException e) {
			e.printStackTrace();
			response = new ResponseEntity<String>(HttpStatus.CONFLICT) ;
		}
		
		return response ;
	}

	
	
	
	
}
