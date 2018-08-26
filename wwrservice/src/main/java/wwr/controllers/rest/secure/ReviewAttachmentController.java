package wwr.controllers.rest.secure;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import wwr.model.enities.Person;
import wwr.model.enities.ReviewAttachment;
import wwr.model.enities.UserDocument;
import wwr.repositories.PersonRepository;
import wwr.repositories.ReviewAttachmentRepository;
import wwr.repositories.UserDocumentRepository;

@RestController
public class ReviewAttachmentController {
	
	@SuppressWarnings("unused")
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReviewAttachmentController.class);

	@Autowired
	private UserDocumentRepository fileRepository;	

	@Autowired
	private ReviewAttachmentRepository ratRepository;	
	
	@Autowired
	private PersonRepository personRepository;


	
	@RequestMapping(value = "/rest/secure/reviewattachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<ReviewAttachment> getMyUserReviewAttachments( ) {
		Iterable<ReviewAttachment> ratlist = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Person pSearch = personRepository.findByNickname(auth.getName()) ;
		ratlist = ratRepository.findAll();
	    return ratlist ;
    }	
	
	@RequestMapping(value = "/rest/secure/reviewattachments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReviewAttachment> saveMyReviewAttachment( @RequestBody ReviewAttachment rat ){
		ReviewAttachment ratNew = null ;
	   ratNew = ratRepository.save(ratNew) ;
	   return ResponseEntity.ok(ratNew);
	}
	
	@RequestMapping(value = "/rest/secure/reviewattachments", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteReviewAttachment(  @RequestParam Long id ){
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(HttpStatus.OK);
		
		ReviewAttachment rat = ratRepository.findById(id).get() ;
		
		
		UserDocument udo = fileRepository.findById(rat.getFileId()).get() ;
		
		try {
			UserDocFileIoController.deleteFile(udo);
			ratRepository.deleteById(id) ;
			fileRepository.delete(udo);
		} catch (IOException e) {
			e.printStackTrace();
			response = new ResponseEntity<String>(HttpStatus.CONFLICT) ;
		}
		

		return response ;
	}
	
	
	
	
	

	
	@RequestMapping(value="/rest/secure/upload/reviewdoc", method = RequestMethod.POST)
    public List<ReviewAttachment> UploadFile(MultipartHttpServletRequest request) throws IOException {
	
	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Person pSearch = personRepository.findByNickname(auth.getName()) ;
       List<ReviewAttachment> ratList = new ArrayList<ReviewAttachment>() ;
       
	   for (Iterator<String> itr = request.getFileNames(); itr.hasNext();) {

		   MultipartFile file=request.getFile(itr.next());			
		   UserDocument udo = new UserDocument();
		   
		   udo.setOwner(pSearch);
		   udo.setDate(new Date());
		   udo.setMediatype(file.getContentType());
		   udo.setFilename(file.getOriginalFilename());
		   
		   UserDocument udon =  fileRepository.save(udo);
		   
		   UserDocFileIoController.writeFile(udon, file.getBytes());
		   
		   ReviewAttachment  rat = new ReviewAttachment() ;
		   rat.setFileId(udon.getId());
		   rat.setUploadfilename( file.getOriginalFilename() );
		   rat.setMediatype(file.getContentType());
		   rat.setFileSize(file.getSize() );
		   
		   ratList.add(rat);
		   
	   }

	   return ratList ;

    }

	public static String getUrl( Long docId ){
		return "rest/secure/documents/" + docId.toString() ;		
	}
	

	@RequestMapping( value = "/rest/secure/documents/{id}" , 
					 method = RequestMethod.GET )
	
	
		public ResponseEntity<byte[]> getFileData( @PathVariable Long id ) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		

		
		UserDocument udo = fileRepository.findById(id).get() ;


		headers.setContentType(MediaType.IMAGE_JPEG);

		if(udo.getFilename().endsWith(".pdf")){
			headers.setContentType(new MediaType("application","pdf"));			
		}
		
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(UserDocFileIoController.readFile(udo), headers, HttpStatus.OK);
		
		headers.setContentLength(response.getBody().length);


		return response  ;
	}
	
	
	
}
