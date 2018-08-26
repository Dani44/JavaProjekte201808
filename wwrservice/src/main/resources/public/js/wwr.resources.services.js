
wwrApplication.factory('CaliberService',  
 function ($resource) {
	 var myResource = $resource('/wwrservice/rest/secure/calibers' ) ;
	 return myResource ; 
 }
)	;

wwrApplication.factory('PrimerTypeService',  
  function ($resource) {
	 var myResource = $resource('/wwrservice/rest/secure/primertypes') ; 
	 return myResource ; 
  }
)	;

wwrApplication.factory('MyRecipeService',  
  function ($resource) {
  	var myResource = $resource('/wwrservice/rest/secure/myrecipies' ) ;
	return myResource ; 
  }
)	;

wwrApplication.factory('MyLotService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/mylots' ) ;
			return myResource ; 
	  }
)	;


wwrApplication.factory('ReviewAttachmentService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/reviewattachments' ) ;
			return myResource ; 
	  }
) ;

wwrApplication.factory('UserDocumentService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/myfiles' ) ;
			return myResource ; 
	  }
) ;


wwrApplication.factory('MyReviewService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/myreviews' ) ;
			return myResource ; 
	  }
) ;

wwrApplication.factory('MySerialsService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/myserials' ) ;
			return myResource ; 
		  }
		)	;





wwrApplication.factory('PersonService',  
		  function ($resource) {
		  	var myResource = $resource('/wwrservice/rest/secure/persons/me' ) ;
			return myResource ; 
		  }
		)	;

