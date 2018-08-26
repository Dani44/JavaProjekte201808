/**
 *  Constructor 
 */
RecipeVC = function( $http , MyRecipeService ){
	this.$http = $http ;
	this.recipies = [] ;
	this.MyRecipeService = MyRecipeService
}




//----------------------------------------------------------------------
// Recipe finden
//----------------------------------------------------------------------
RecipeVC.prototype.getRecipeById = function( id ){
	var i = null ;
	for( i = 0 ; i < this.recipies.length ; i++ ){
		if(this.recipies[i].id == id ){
			return this.recipies[i] ;
		}
	}
	return null ;
}	


//--------------------------------------------------------------------------------------------------------------------------------------
//  Index eines Objekts finden
//--------------------------------------------------------------------------------------------------------------------------------------

	//----------------------------------------------------------------------
	//Index eines Recipe finden
	//----------------------------------------------------------------------
	RecipeVC.prototype.getIndexOfRecipe = function( id ){
		var i = null ;
		for( i = 0 ; i < this.recipies.length ; i++ ){
			if(this.recipies[i].id == id ){
				return i ;
			}
		}
		return i ;
	}	

	//----------------------------------------------------------------------
	//Index eines Lots eines übergebenen Recipe finden
	//----------------------------------------------------------------------
	RecipeVC.prototype.getLotIndex = function( pRecipe , pLotId ){
		var iReturn = null ;
		for( i=0 ; i < pRecipe.lots.length ; i++ ){
			if( pRecipe.lots[i].id == pLotId ){
				return i ;	
			}		 
		}
		return iReturn ;
	}
	
	//----------------------------------------------------------------------
	//Index eines übergebenen Review finden
	//----------------------------------------------------------------------
	RecipeVC.prototype.getReviewIndex = function( pRecipe , pReviewId ){
		var iReturn = null ;
		for( i=0 ; i < pRecipe.reviews.length ; i++ ){
			if( pRecipe.reviews[i].id == pReviewId ){
				return i ;	
			}		 
		}
		return iReturn ;
	}


	//----------------------------------------------------------------------
	//Index eines Lot finden
	//----------------------------------------------------------------------
	RecipeVC.prototype.getIndexOfLot = function( pRecipeId , pLotId ){
		var iReturn = null ;
		var idRec = this.getIndexOfRecipe( pRecipeId ) ;
		for( i=0 ; i < this.recipies[idRec].lots.length ; i++ ){
			if( this.recipies[idRec].lots[i].id == pLotId ){
				return i ;	
			}		 
		}
		return iReturn ;
	}

	//----------------------------------------------------------------------
	//Index eines Review finden
	//----------------------------------------------------------------------
	RecipeVC.prototype.getIndexOfReview = function( pRecipeId , pReviewId ){
		var iReturn = null ;
		var idRec = this.getIndexOfRecipe( pRecipeId ) ;
		for( i=0 ; i < this.recipies[idRec].reviews.length ; i++ ){
			if( this.recipies[idRec].reviews[i].id == pReviewId ){
				return i ;	
			}		 
		}
		return iReturn ;
	}

//--------------------------------------------------------------------------------------------------------------------------------------
// Get Childobjekts of Objects
//--------------------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------
	// Get Lot in a Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.getRecipeLot = function( pRecipeId , pLotId ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		if( indexRecipe >= 0 ){
			var indexLot = this.getIndexOfLot( pRecipeId , pLotId  ) ;
			if( indexLot >= 0 ){
				return this.recipies[indexRecipe].lots[ indexLot ] ;
			}
		}
		return null ;
	}

	
    //----------------------------------------------------------------------
	// Get Review in a Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.getRecipeReview = function( pRecipeId , pReviewId ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		if( indexRecipe >= 0 ){
			var indexReview = this.getIndexOfReview( pRecipeId , pReviewId  ) ;
			if( indexReview >= 0 ){
				return this.recipies[indexRecipe].reviews[ indexReview ] ;
			}
		}
		return null ;
	}
	
	
	
//--------------------------------------------------------------------------------------------------------------------------------------
// Update Objekts
//--------------------------------------------------------------------------------------------------------------------------------------

	//----------------------------------------------------------------------
	// Update Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.updateRecipe = function( pRecipe ){
		var indexRecipe = this.getIndexOfRecipe( pRecipe.id ) ;
		if( indexRecipe >= 0 ){
			this.recipies[indexRecipe] = pRecipe ;
		}	
	}
	
	//----------------------------------------------------------------------
	// Update Lot 
	//----------------------------------------------------------------------
	RecipeVC.prototype.updateRecipeLot = function( pRecipeId , pLot ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		var indexLot    = this.getIndexOfLot( pRecipeId , pLot.id ) ;
		this.recipies[indexRecipe].lots[indexLot] = pLot ;	
	}

	//----------------------------------------------------------------------
	// Update Review 
	//----------------------------------------------------------------------
	RecipeVC.prototype.updateRecipeReview = function( pRecipeId , pReview ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		var indexReview    = this.getIndexOfReview( pRecipeId , pReview.id ) ;
		this.recipies[indexRecipe].reviews[indexReview] = pReview ;	
	}

//--------------------------------------------------------------------------------------------------------------------------------------
// Delete Objekts
//--------------------------------------------------------------------------------------------------------------------------------------

	//Delete a Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.deleteRecipe = function( pRecipeId ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		if( indexRecipe >= 0 ){
			this.recipies.splice( indexRecipe , 1 ) ;
		}	
	}
	
	// Delete Lot in a Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.deleteRecipeLot = function( pRecipeId , pLotId ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		if( indexRecipe >= 0 ){
			var indexLot = this.getIndexOfLot( pRecipeId , pLotId  ) ;
			if( indexLot >= 0 ){
				this.recipies[indexRecipe].lots.splice( indexLot, 1 ) ;
			}
		}	
	}
	
	//Delete Review in a Recipe
	//----------------------------------------------------------------------
	RecipeVC.prototype.deleteRecipeReview = function( pRecipeId , pReviewId ){
		var indexRecipe = this.getIndexOfRecipe( pRecipeId ) ;
		if( indexRecipe >= 0 ){
			var indexReview = this.getIndexOfReview( pRecipeId , pReviewId  ) ;
			if( indexReview >= 0 ){
				this.recipies[indexRecipe].reviews.splice( indexReview, 1 ) ;
			}
		}	
	}



//--------------------------------------------------------------------------------------------------------------------------------------
// Create Objekts
//--------------------------------------------------------------------------------------------------------------------------------------


	RecipeVC.prototype.getNewRecipe = function( pOwner ){
	return {	"id":null,
			 	"owner": pOwner ,
				"caliber":null,
				"bulletMfg":null,
				"bulletType":null,
				"bulletDia":null,
				"bulletWeightGrains":null,
				"powderMfg":null,
				"powderShortName":null,
				"powderMin":null,
				"powderLoad":null,
				"powderMax":null,
				"oalInches":null,
				"oalmm":null,
				"lots":[],
				"reviews":[],				
				"visibleState":1 } ;
	}			

	RecipeVC.prototype.getNewLot = function( ){	
		return { "date" : new Date(),
			     "note":null } ;
	}		  
	
	RecipeVC.prototype.getNewReview = function( ){
		return {	"id":null,
				 	"date": new Date() ,
				 	"reliability" : 0 ,
				 	"burn" : 0 ,
				 	"power" : 0 ,
				 	"accuracy" : 0 ,				 					 	
				 	"summary" : 0 ,				 	
					"note":null ,
					"attachments" : [] } ;
	}			


	RecipeVC.prototype.getNewUserDocument = function( ){
		return {	"id":null,
			        "date": new Date() ,
			        "owner": null ,
			        "filename": null,
			        "mediatype": null} ;
	}			
	
	RecipeVC.prototype.getNewReviewAttachment = function( review ){
		return {	"id":null,
			        "fileId": null ,
			        "uploadfilename": null,
			        "mediatype": null,
			        "fileSize" : null , 
			        "sequence": review.attachments.length + 1,
			        "decription": null	,
			        "url":null
		} ;
	}			
	
	