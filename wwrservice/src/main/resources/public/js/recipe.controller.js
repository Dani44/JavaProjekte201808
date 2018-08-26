wwrApplication.controller('RecipeController'  ,
						   function($rootScope, 
								    $scope, 
								    $http ,
								    $route,
								    $filter,
								    MyRecipeService,
								    MyLotService,
								    MyReviewService,
								    ReviewAttachmentService,
								    PersonService , 
								    uiGridConstants,
								    uiGridGroupingConstants ,
								    $uibModal,
								    $location,
								    $routeParams,
								    $timeout,
								    $q, 
								    $interval , $log) {


	
	if( ! $rootScope.authenticated ){
		$rootScope.requestedPage = "/recipies" ;
		$location.path('/login');
		return ;
	}


	//--------------------------------------------------------------------------------------------
	// Variables 
	//--------------------------------------------------------------------------------------------
	$scope.lot = {} ;
	$scope.lots = [] ;
	$scope.activeView = 0 ;
	$scope.activeRecipeRow = null ;

	$scope.vc = new RecipeVC( $http , MyRecipeService ) ;
	
	
	//--------------------------------------------------------------------------------------------
	// Test Function (Prototyping) 
	//--------------------------------------------------------------------------------------------
	$scope.testFunc = function(){
		var idRec = $scope.vc.getIndexOfRecipe( 1 ) ;
		var lor = $scope.vc.getRecipeLot( 1, 2 );

	}
	

	//--------------------------------------------------------------------------------------------
	// gridOption for Review
	//--------------------------------------------------------------------------------------------
	$scope.gridOptionsReview = {
			data: 'activeRecipeRow.reviews' ,			
			multiSelect: false ,
			enableColumnResizing: true,
			enableFullRowSelection: false,
			enableRowSelection: true,
		    
		    selectionRowHeaderWidth: 35,
		    rowHeight: 35,
			
		    columnDefs: [  { field: 'editBtn' ,
	           	 			 enableFiltering: false ,
	           	 			 displayName: '' ,
	           	 			 maxWidth: 35 ,minWidth: 35 ,
	           	 			 enableColumnMenu: false,
	           	 			 cellTemplate: '<button type="button" class="btn btn-info btn-sm" '+
	           	 			               ' uib-tooltip="Edit this Review" ' +
 	           	 				           ' ng-click="grid.appScope.editReviewRow(row)" ng-show="row.entity.id != null">'+
 	           	 				       		'<span class="glyphicon glyphicon-pencil"></span>'+
  	           	                  '</button>' }
		    				,{ field: 'date'    	         , displayName: "Loaded at"  , 
						       cellFilter: 'date:\'dd.MM.yyyy\'' , 
						       enableCellEdit: false , 
						       width:100 , 
						       sort: { direction: uiGridConstants.DESC }}
		    			   ,{ field: 'id'    	         , displayName: "Id"   , enableCellEdit: false , width:50  , sort: { direction: uiGridConstants.DESC }  }
			   			   ,{ field: 'gun'    	         , displayName: "Gun"      , enableCellEdit: false , width:130 }
			   			   ,{ field: 'summary'           ,
		           	 			 displayName: 'Rating Summary' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 cellTemplate: '<span uib-rating max="10" ng-model="row.entity.summary" read-only="true"></span>'}
			   			   ,{ field: 'reliability' ,
		           	 			 displayName: 'Reliability' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 cellTemplate: '<span uib-rating max="10" ng-model="row.entity.reliability" read-only="true"></span>'}

			   			   ,{ field: 'accuracy' ,
		           	 			 displayName: 'Accuracy' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 cellTemplate: '<span uib-rating max="10" ng-model="row.entity.accuracy" read-only="true"></span>'}
			   			   ,{ field: 'power' ,
		           	 			 displayName: 'Power' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 cellTemplate: '<span uib-rating max="10" ng-model="row.entity.power" read-only="true"></span>'}
			   			   ,{ field: 'burn' ,
		           	 			 displayName: 'Burn Behavior' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 cellTemplate: '<span uib-rating max="10" ng-model="row.entity.burn" read-only="true"></span>'}
		    			   		    			   
		                ] ,
	
		    onRegisterApi: function(gridApi){			    	
				$scope.gridApiReview = gridApi;
				$scope.loadData() ;	
				gridApi.rowEdit.on.saveRow($scope, $scope.saveReviewRow);				
				
		    }
	
     }; 	 	
	

	//--------------------------------------------------------------------------------------------
	// gridOption for Lots 
	//--------------------------------------------------------------------------------------------
	$scope.gridOptionsLot = {
			data: 'activeRecipeRow.lots' ,			
			multiSelect: false ,
			enableColumnResizing: true,
			enableFullRowSelection: false,
			enableRowSelection: true,
		    
		    selectionRowHeaderWidth: 35,
		    rowHeight: 35,
			
		    columnDefs: [  { field: 'editBtn' ,
	           	 			 enableFiltering: false ,
	           	 			 displayName: '' ,
	           	 			 maxWidth: 35 ,minWidth: 35 ,
	           	 			 enableColumnMenu: false,
	           	 			 cellTemplate: '<button type="button" class="btn btn-info btn-sm" '+
 	           	 				           ' ng-click="grid.appScope.editLogRow(row)" ng-show="row.entity.id != null">'+
 	           	 				       		'<span class="glyphicon glyphicon-pencil"></span>'+
  	           	                  '</button>' }
			   			   ,{ field: 'date'    	         , displayName: "Loaded at"  , cellFilter: 'date:\'dd.MM.yyyy\''  }		    
		    			   ,{ field: 'casebrand'    	 , displayName: "Case"       }
		    			   ,{ field: 'primer'    	     , displayName: "Primer"     }
		    			   ,{ field: 'crimp'    	     , displayName: "Crimp"      }
		    			   ,{ field: 'size'    	         , displayName: "Lot Size"   }		    			   
		    			   ,{ field: 'note'    	         , displayName: "Reloading Note"       }		    			   
		                ] ,
	
		    onRegisterApi: function(gridApi){			    	
				$scope.gridApiLot = gridApi;
				$scope.loadData() ;	
				gridApi.rowEdit.on.saveRow($scope, $scope.saveLotRow);				
				
				
		    }
	
     }; 	 

	
	 //--------------------------------------------------------------------------------------------
	 // Create new Recipe
	 //--------------------------------------------------------------------------------------------
     $scope.createNewRecipe = function( pbClone ){   	 
	   	 if(pbClone){
	   		 if( $scope.gridApi.selection.getSelectedRows().length > 0  ){
	   			 angular.copy($scope.gridApi.selection.getSelectedRows()[0],$scope.editorRecipe);
	   			 $scope.editorRecipe.id = null ;    			 
	   			 $scope.editorRecipe.lots = [] ;
	   			 $scope.editorRecipe.reviews = [] ;    			 
	   		 }
	   	 } else {
	       	 $scope.editorRecipe = $scope.vc.getNewRecipe( $rootScope.ich ) ;
   			 $scope.editorRecipe.id = null ;    			 
   			 $scope.editorRecipe.lots = [] ;
   			 $scope.editorRecipe.reviews = [] ;    			 
	   	 }

	   	 $scope.openRecipe( $scope.editorRecipe ) ;    	 
     };

	
	
	
	
	//--------------------------------------------------------------------------------------------
	// Create a new lot
	//--------------------------------------------------------------------------------------------
	$scope.createNewLot = function( pbClone ){
		
	   	 if( pbClone ){
			 if( $scope.gridApiLot.selection.getSelectedRows().length > 0  ){
				 $scope.newlot = $scope.vc.getNewLot() ;
				 
				 angular.copy($scope.gridApiLot.selection.getSelectedRows()[0],$scope.newlot);
				 $scope.newlot.id = null ;    			 
				 $scope.newlot.date = new Date() ;
					$scope.newlot.oalmm =  $scope.activeRecipeRow.oalmm ;
					$scope.newlot.oalInches =  $scope.activeRecipeRow.oalInches ;

			 }
		 } else {
				$scope.newlot = $scope.vc.getNewLot() ;    		 
				$scope.newlot.oalmm =  $scope.activeRecipeRow.oalmm ;
				$scope.newlot.oalInches =  $scope.activeRecipeRow.oalInches ;

		 }


		$scope.openLot( $scope.newlot ) ;
	} 


	//--------------------------------------------------------------------------------------------
	// Create a new Review
	//--------------------------------------------------------------------------------------------
	$scope.createNewReview = function( pbClone  ){

	   	 if( pbClone ){
			 if( $scope.gridApiReview.selection.getSelectedRows().length > 0  ){
				 $scope.newReview  = $scope.vc.getNewReview() ;
				 angular.copy($scope.gridApiReview.selection.getSelectedRows()[0],$scope.newReview);
				 $scope.newReview.id = null ;    			 
				 $scope.newReview.date = new Date() ;
				 $scope.newReview.lots = [] ;
				 $scope.newReview.attachments = [] ;
				 
			 }
		 } else {
			$scope.newReview = $scope.vc.getNewReview() ;    		 
		 }
		
		

		$scope.openReview( $scope.newReview ) ;
	} 

	
	
	
	//--------------------------------------------------------------------------------------------
	// Gridoptions Mobile
	//--------------------------------------------------------------------------------------------
	$scope.gridOptionsMobile = {
			data: 'vc.recipies' ,			
			showHeader: false ,
		    rowHeight: 200,
			
		    columnDefs: [  { field: 'avgSumRating'       ,
  	 			 			 displayName: 'Rating counter' ,
			   	 			 width: 190 ,
			   	 			 enableColumnMenu: false,
			   	 			 enableCellEdit: false ,
			   	 			 cellTemplate: '<span uib-rating max="10" ' + 
			   	 				 		   'ng-model="row.entity.reviews.length" '+
			   	 				 		   'ng-click="grid.appScope.zoomRow(row)" '+
			   	 				 		   'read-only="true"></span>'}
		                  ],
		                  
		    onRegisterApi: function(gridApi){			    	
	    					$scope.gridApi = gridApi;
	    					$scope.loadData() ;	
	    					gridApi.rowEdit.on.saveRow($scope, $scope.saveRecipeRow) }

		                  
	} ;
	
	$scope.gridOptions = {
			data: 'vc.recipies' ,			
			groupColumn:'caliber',

			enableFiltering: screen.availHeight > 800  ,
			enableGridMenu: true,
			
			exporterMenuCsv: false,
			exporterCsvFilename: 'myFile.csv',


			multiSelect: false ,
			enableColumnResizing: true,
			enableFullRowSelection: false,
			enableRowSelection: true,

			enableSelectAll: true,
		    enableCellEdit: true ,
		    
		    selectionRowHeaderWidth: 32,
		    rowHeight: 32,
		    showGridFooter:true,
			
		    columnDefs: [  { field: 'editBtn' ,
				 	           	 	enableFiltering: false ,
				 	           	 	displayName: '' ,
				 	           	 	maxWidth: 34 ,minWidth: 34 ,
				 	           	 	enableColumnMenu: false,
				 	           	 	cellTemplate: '<button type="button" class="btn btn-default btn-sm" '+
				 	           	 					   ' uib-tooltip="edit this recipe" ' +
				 	           	 				       ' ng-click="grid.appScope.editRow(row)" ng-show="row.entity.id != null">'+
				 	           	 				       		'<span class="glyphicon glyphicon-pencil"></span>'+
				  	           	                  '</button>'  	           	          
				  	        }
		    			   ,{ field: 'zoomBtn' ,
		    				   		 visible: true ,
		    				   		 enableFiltering: false ,
		    				   		 displayName: '' ,
		    				   		 maxWidth: 34 ,minWidth: 34 ,
		    				   		 enableColumnMenu: false,
		    				   		 cellTemplate: '<button type="button" class="btn btn-default btn-sm" ng-click="grid.appScope.zoomRow(row)" ng-show="row.entity.id != null">'+
	           	               				 	'<span class="glyphicon glyphicon-zoom-in"></span>'+
	           	               				 	'</button>'
		    			    }
		    			   
		    			  ,{ field: 'id' 		     	 , displayName: "Id" , 		   enableCellEdit: false , width:50 }		                    
		    			  ,{ field: 'caliber' 		     , displayName: "Caliber"    , width:	150	}
		                  ,{ field: 'bulletMfg'    		 , displayName: "Bullet Mfg" , width:	100 , visible: screen.availWidth > 1600  }
		                  ,{ field: 'bulletType'   		 , displayName: "Type"       , width:	100	}
		                  ,{ field: 'bulletWeightGrains' , displayName: "Weight"     , width:	100	}		                  
		                  ,{ field: 'bulletDia'    		 , displayName: "Bullet Dia"	 , width:	100	} 
		                  ,{ field: 'powderMfg'    		 , displayName: "Powder Mfg" , width:	100	, visible: screen.availWidth > 1600 } 
		                  ,{ field: 'powderShortName'    , displayName: "Powder"  	 , width:	100	, visible: screen.availWidth > 1600	} 
		                  ,{ field: 'powderLoad' 		 , displayName: "Charge"  	 , width:	100		} 
		                  ,{ field: 'primerType' 		 , displayName: "Primer"  	 , width:	100	, visible: screen.availWidth > 1600	}		                  
		                  ,{ field: 'oalInches' 		 , displayName: "OAL Inch" 	 , width:	100	, visible: screen.availWidth > 1600	}		                  
		                  ,{ field: 'oalmm' 		     , displayName: "OAL mm"   	 , width:	100	}
			   			  ,{ field: 'avgSumRating'       ,
		           	 			 displayName: 'Rating' ,
		           	 			 width: 190 ,
		           	 			 enableColumnMenu: false,
		           	 			 enableCellEdit: false ,
		           	 			 cellTemplate: '<span uib-rating max="10" ' + 
		           	 				 		   'ng-model="row.entity.avgRating" '+
		           	 				 		   'ng-click="grid.appScope.zoomRow(row)" '+
		           	 				 		   'read-only="true"></span>'}
		                  
		                  ,{ field: 'note' 		         , displayName: "Note"   	 , width:	300	, visible: screen.availWidth > 1600	}		                  
		                  ],
		                  
		    onRegisterApi: function(gridApi){			    	
	    					$scope.gridApi = gridApi;
	    					$scope.loadData() ;	
	    					gridApi.rowEdit.on.saveRow($scope, $scope.saveRecipeRow) }

		                  
	} ;

	
	
	//--------------------------------------------------------------------------------------------
	// Get Data
	//--------------------------------------------------------------------------------------------
	$scope.restoreGridLayout = function(){
	      if (localStorage.RecipeGrid_V2 != null){
	    	 $scope.gridApi.saveState.restore( $scope, JSON.parse( localStorage.RecipeGrid_V2 ) );				  
	      }
	}	

	
	$scope.loadData = function(){
		MyRecipeService.query().$promise
			  .then(function(result) 	{ $scope.vc.recipies = result ;
			  							  $scope.restoreGridLayout() ;			  							  
			  							}, 
				    function(errorMsg)  { console.log("error")   ; }
				   );	
	}	
	

	
	
	

	 $scope.saveState = function() {
   	    localStorage.setItem( 'RecipeGrid_V2', JSON.stringify($scope.gridApi.saveState.save())); 
	 };
		 
    $scope.restoreState = function() {
	    if (localStorage.RecipeGrid_V2 != null){
		    $scope.gridApi.saveState.restore( $scope, JSON.parse( localStorage.RecipeGrid_V2 ) );				  
		}
	 };

    $scope.resetState = function() {
	    if (localStorage.RecipeGrid_V2 != null){ localStorage.removeItem("RecipeGrid_V2"); }
	    $route.reload();			    
	 };

	 
	 
	 //--------------------------------------------------------------------------------------------
	 // Confirm: Delete selected Recipe
	 //--------------------------------------------------------------------------------------------
	 $scope.deleteRecipies = function(){

		 var recipe = $scope.vc.getRecipeById($scope.gridApi.selection.getSelectedRows()[0].id) ;
		 
		 var promise = $q.defer();
		 
		 MyRecipeService.delete( recipe )
		 .$promise
			.then( 	function( result ) { $scope.vc.deleteRecipe( recipe.id ) ;
										 promise.resolve(); }, 
					function(errorMsg) { 	 alert(errorMsg) ;
											 promise.reject() ;} 
	              ) ;		 		 				
	 }

	//--------------------------------------------------------------------------------------------
	// Confirm: Delete selected Lot
	//--------------------------------------------------------------------------------------------
	 $scope.deleteLots = function(){
		 if( $scope.gridApiLot.selection.getSelectedRows().length > 0  ){
			
			 var idRecipe = $scope.activeRecipeRow.id ;
			 var idLot   = $scope.gridApiLot.selection.getSelectedRows()[0].id ;
			 var lot = $scope.vc.getRecipeLot( idRecipe , idLot) ;
			 
		     var promise = $q.defer();

		     MyLotService.delete( lot )
	    			.$promise
	    			.then( 	function( result ) { $scope.vc.deleteRecipeLot( idRecipe , idLot) ;
	    									     $scope.activeRecipeRow = $scope.vc.getRecipeById(idRecipe) ; 
	    										 promise.resolve(); }, 
	    					function(errorMsg) { promise.reject() ;} 
			              ) ;
		 }
		 
	 }


		//--------------------------------------------------------------------------------------------
		// Confirm: Delete selected Reviews
		//--------------------------------------------------------------------------------------------
		 $scope.deleteReviews = function(){
			 if( $scope.gridApiReview.selection.getSelectedRows().length > 0  ){
				

				 var idRecipe = $scope.activeRecipeRow.id ;
				 var idReview   = $scope.gridApiReview.selection.getSelectedRows()[0].id ;
				 var review = $scope.vc.getRecipeReview( idRecipe , idReview) ;

		         var promise = $q.defer();

			     MyReviewService.delete( review )
		    			.$promise
		    			.then( 	function( result ) { $scope.vc.deleteRecipeReview( idRecipe , idReview) ;
		    									     $scope.activeRecipeRow = $scope.vc.getRecipeById(idRecipe) ; 
		    										 promise.resolve(); }, 
		    					function(errorMsg) { promise.reject() ;} 
				              ) ;
			 }
			 
		 }

	 
	 
	 
	//--------------------------------------------------------------------------------------------
	// Confirm: Delete Rows
	//--------------------------------------------------------------------------------------------
	$scope.deleteRows = function( rowType  ){
		 
		 var myTemplateUrl = '' ;
		 
		 var myController  = 'RecipeDeleteConfirm' ;
		 
		 if( rowType == 'lot'){
			 myTemplateUrl = 'lot_delete_confirm.html' ;
		 } else if(rowType == 'review') {
			 myTemplateUrl = 'review_delete_confirm.html' ;		 
		 } else if(rowType == 'recipe') {
			 myTemplateUrl = 'recipe_delete_confirm.html' ;			 
		 } else {
			 alert("Error: undefifined rowtype parameter in scope.deleteRows()") ;
		 }
		 
		
		 var modalInstance = $uibModal.open({ animation: true ,
			  templateUrl: myTemplateUrl ,
			  controller: myController  ,
		      size: 'sm' ,
			  resolve: { prec: function () { return true ; } }
			});

		 if( rowType == 'lot'){
			 modalInstance.result.then(	function () { $scope.deleteLots() ; }     , function () { } );
		 } else if(rowType == 'review') {
		 	 modalInstance.result.then(	function () { $scope.deleteReviews() ; }     , function () { } );		
		 } else if(rowType == 'recipe') {
			 modalInstance.result.then(	function () { $scope.deleteRecipies() ; } , function () { } );		
		 } else {
			 alert("Error: undefifined rowtype parameter in scope.deleteRows()") ;	    	 
		 }
		
	};
	
	
    
	
	//--------------------------------------------------------------------------------------------
	// Recipe: Save bei direktem Grid-Editieren
	//--------------------------------------------------------------------------------------------
    $scope.saveRecipeRow = function( rowEntity ) {
    	
    	var promise = $q.defer();
    	
    	
    	$scope.gridApi.rowEdit.setSavePromise( rowEntity, promise.promise );      
    	rowEntity.oalInches = Math.ceil( (rowEntity.oalmm / 25.4)  * 1000 ) / 1000 ;        
    	
    	MyRecipeService.save( rowEntity)
    		.$promise
    		.then( function( result ) { promise.resolve(); }, 
    			   function(errorMsg) { promise.reject() ;}
    	);
            	
    };

    
	//--------------------------------------------------------------------------------------------
	// Review: Save bei direktem Grid-Editieren
	//--------------------------------------------------------------------------------------------
    $scope.saveReviewRow = function( rowEntity ) {
    	// ToDo
    	alert("Save not implemented");
    }

    
    
	//--------------------------------------------------------------------------------------------
	// Lot: Save bei direktem Grid-Editieren
	//--------------------------------------------------------------------------------------------
    $scope.saveLotRow = function( rowEntity ) {
    	var promise = $q.defer();    	
    	$scope.gridApiLot.rowEdit.setSavePromise( rowEntity, promise.promise );
    	var recipeIndex = $scope.vc.getIndexOfRecipe($scope.activeRecipeRow.id) ;
    	MyRecipeService.save( $scope.vc.recipies[recipeIndex] )
		.$promise
		.then( function( result ) { $scope.vc.recipies[recipeIndex] = result ; 
									promise.resolve(); }, 
			   function(errorMsg) { promise.reject() ;}
	    );
    };

    
    
    
    //--------------------------------------------------------------------------------------------
	// OAL Converter
	//-------------------------------------------------------------------------------------------- 
    $scope.convertOal1= function(  ) {
       		$scope.editorRecipe.oalInches = Math.ceil( ($scope.editorRecipe.oalmm     / 25.4)  * 1000 ) / 1000 ;
    }
   
	$scope.convertOal2= function(  ) {
		$scope.editorRecipe.oalmm     = Math.ceil( ($scope.editorRecipe.oalInches * 25.4)  * 100  ) /  100 ;
	}
    
	
    //--------------------------------------------------------------------------------------------
	// Refresh the Grid
	//--------------------------------------------------------------------------------------------
	 $scope.refreshGrid = function(){
		 $route.reload();
     };

	 //--------------------------------------------------------------------------------------------
	 // Edit Recipe
	 //--------------------------------------------------------------------------------------------
     $scope.editRow = function( erow ){
    	 if( erow.entity.id != null){    		 
	   		 $scope.gridApi.selection.selectRow(erow.entity ) ;    		 
    		 $scope.openRecipe( erow.entity ) ;    		 
    	 }
     };

	 //--------------------------------------------------------------------------------------------
	 // Edit Lot
	 //--------------------------------------------------------------------------------------------
     $scope.editLogRow = function( erow ){
    	 if( erow.entity.id != null){    		 
    		 $scope.openLot( erow.entity ) ;    		 
    	 }
     };
     
	 //--------------------------------------------------------------------------------------------
	 // Edit Review
	 //--------------------------------------------------------------------------------------------
     $scope.editReviewRow = function( erow ){
    	 if( erow.entity.id != null){    		 
    		 $scope.openReview( erow.entity ) ;    		 
    	 }
     };
     
     

     
     //--------------------------------------------------------------------------------------------
 	 // Open Recipe Details Lot
 	 //--------------------------------------------------------------------------------------------
     $scope.activateView = function( viewNo ){
   		 $scope.activeView = viewNo ;
   		 $scope.gridApi.core.refresh();  		 
     }
     
     
     
     
    //--------------------------------------------------------------------------------------------
	// Zoom Recipe
	//--------------------------------------------------------------------------------------------
    $scope.zoomRow = function( erow ){
	   	 if( erow.entity.id != null){
	   		 $scope.gridApi.selection.selectRow(erow.entity ) ;
	   		 $scope.activeRecipeRow = erow.entity ;
	   		 $scope.activateView(1) ;
	   	 }
    };

           
           
       	//--------------------------------------------------------------------------------------------
       	// Switch Initialize ....
       	//--------------------------------------------------------------------------------------------
       	$scope.init = function( ){
       		$scope.errorMsg   = "" ;
       		$scope.editorRecipe = {} ;
       		$scope.bufferRecipe = {} ;
       		
       		// Delete old configs 
       		if (localStorage.stateV100 != null){ localStorage.removeItem("stateV100"); }
       	
       	}
       	
       //--------------------------------------------------------------------------------------------
       // Initialize Variables
       //--------------------------------------------------------------------------------------------
       	$scope.init() ;
       	
       	$scope.toggleFiltering = function(){
       	    $scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering;
       	    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
       	}; 	
      
//--------------------------------------------------------------------------------------------
// Modales Recipe Editor Fenster Ã¶ffnen
//--------------------------------------------------------------------------------------------        
     $scope.openRecipe = function ( pRecipeIn ) {
    	 
    	 var editorCopy = {} ;
    	 angular.copy( pRecipeIn , editorCopy ) ;
    	 

		 var modalInstance = $uibModal.open({ animation: true ,
			 								  templateUrl: 'myModalRecipeEdit.html',
			 								  controller:  'ModalRecipeEditCtrl',
			 							      // size: 'lg' ,
			 								  resolve: { prec: function () { return editorCopy ; } }
		 									});

		 modalInstance.result.then(

			function ( reciReturn ) {
		    	
		    	reciReturn.oalInches = Math.ceil( (reciReturn.oalmm / 25.4 ) * 1000 ) / 1000 ;
			
				MyRecipeService.save( reciReturn )
				.$promise
				.then(   function( result ) { if( reciReturn.id != null  ){ 
					  							  $scope.vc.updateRecipe( result  )
											  } else {
											      $scope.vc.recipies.push( result ) ;											  
											  }
											  $scope.activeRecipeRow = result ;
											} 			
					   , function(errorMsg) { $scope.errorMsg   = errorMsg;  } );
		    	
		    }, 
			function () {
			 	// $log.info('Modal dismissed at: ' + new Date());
		    }
		 );
     };
  
  //--------------------------------------------------------------------------------------------
  // Modales Lot Editor Fenster Ã¶ffnen
  //--------------------------------------------------------------------------------------------
  $scope.openLot = function ( pLotIn ) {

	 	 var editorCopy = {} ;
		 angular.copy( pLotIn  , editorCopy ) ;
  
  		 var modalInstance = $uibModal.open({ animation: true ,
  			 								  templateUrl: 'myModalLotEdit.html',
  			 								  controller:  'ModalLotEditCtrl',
  			 							      // size: 'lg' ,
  			 								  resolve: { prec: function () { return editorCopy ; } }
  		 									});

  		 modalInstance.result.then(
  		    function ( lotReturn ) {
  		    	
  		    	if( lotReturn.id != null){
  		    		var ind = $scope.vc.getLotIndex( $scope.activeRecipeRow , lotReturn.id) ;
  		    		$scope.activeRecipeRow.lots[ind] = lotReturn ;  		    		
  		    	} else {
  		    		$scope.activeRecipeRow.lots.push( lotReturn ) ;
  		    	}
 		    		
  		    	var promise = $q.defer();    	
	  		  	    
	  		    MyRecipeService.save( $scope.activeRecipeRow )
	  		  		.$promise
	  		  		.then( function( result ) { $scope.vc.updateRecipe( result ) ;
	  		  									$scope.activeRecipeRow = result  ;
	  		  									promise.resolve(); }, 
	  		  			   function(errorMsg) { promise.reject() ;}
	  		  	    ); 		    		
  		    	
  		    }, 
  			function () {
  			 	// $log.info('Modal dismissed at: ' + new Date());
  		    }
  		 );
   };

   
   //--------------------------------------------------------------------------------------------
   // Modales Review Editor Fenster Ã¶ffnen
   //--------------------------------------------------------------------------------------------
   $scope.openReview = function ( pReviewIn ) {

 	 	 var editorCopy = {} ;
 		 angular.copy( pReviewIn  , editorCopy ) ;
 		 editorCopy.delAttachments = []; 

 		 
   		 var modalInstance = $uibModal.open({ animation: true ,
   			 								  templateUrl: 'myModalReviewEdit.html',
   			 								  controller:  'ModalReviewEditCtrl',
   			 							      // size: 'lg' ,
   			 								  resolve: { prec: function () { return editorCopy ; } }
   		 									});

   		 modalInstance.result.then(
   		    function ( reviewReturn ) {

   		    	// Alle temporÃ¤ren Proerties aus den Attachments verarbeiten und lÃ¶schen
   		    	//----------------------------------------------------------------------

   		    	var orgAttachments = [] ;
   		    	var delAttachments = [] ;
		    	var newAttachments = [] ;
		    	
		    	angular.copy( reviewReturn.Attachments    , orgAttachments ) ;
		    	
		    	angular.copy( reviewReturn.delAttachments , delAttachments ) ;
		    	delete reviewReturn.delAttachments ;



   		    	
   		    	if( reviewReturn.id != null){
   		    		var ind = $scope.vc.getReviewIndex( $scope.activeRecipeRow , reviewReturn.id) ;
   		    		$scope.activeRecipeRow.reviews[ind] = reviewReturn ;  		    		
   		    	} else {
   		    		$scope.activeRecipeRow.reviews.push( reviewReturn ) ;
   		    	}
  		    		
   		    	var promise = $q.defer();   		    	

   		    	MyRecipeService.save( $scope.activeRecipeRow )
 	  		  		.$promise
 	  		  		.then( function( result ) { $scope.vc.updateRecipe( result ) ;
 	  		  									$scope.activeRecipeRow = result  ; 	  		  									
 	  		  									promise.resolve(); 
 	  		  								   }, 
 	  		  			   function(errorMsg) { promise.reject() ;}
 	  		  	    ); 		    		
 	  		    
 	  		    
 	  		    
 	  		    
 	  		    
 	  		    
 	  		    
   		    	
   		    }, 
   			function () {
   			 	// $log.info('Modal dismissed at: ' + new Date());
   		    }
   		 );
    };
   
   
   
}) // End Of wwrApplication.controller


//--------------------------------------------------------------------------------------------
// Modal Edit Recipe Controller
//-------------------------------------------------------------------------------------------- 
wwrApplication.controller('ModalRecipeEditCtrl', function ( $scope, 
															$uibModalInstance,
															prec ) {

  $scope.rec = prec;
  if( $scope.rec.lots.length > 0 ){
	  $scope.lot = $scope.rec.lots[0];
  }
  
  $scope.ok = function () {
     $uibModalInstance.close($scope.rec);
  };

  $scope.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };
  
  //--------------------------------------------------------------------------------------------
  // OAL Converter
  //-------------------------------------------------------------------------------------------- 
  $scope.oalMm2Inch= function(  ) {
     $scope.rec.oalInches = Math.ceil( ($scope.rec.oalmm     / 25.4)  * 1000 ) / 1000 ;
     
  }
 
  $scope.oalInch2Mm= function(  ) {
	$scope.rec.oalmm     = Math.ceil( ($scope.rec.oalInches * 25.4)  * 100  ) /  100 ;
  } 
 
});


//--------------------------------------------------------------------------------------------
// Modal Edit Lot Controller
//-------------------------------------------------------------------------------------------- 
wwrApplication.controller('ModalLotEditCtrl', function ( $scope,
														 $uibModalInstance , 
														 prec ) {

	$scope.lot = prec;
	$scope.lot.date = new Date($scope.lot.date) ;

	$scope.ok = function () {
	  $uibModalInstance.close($scope.lot);
	};
	
	$scope.cancel = function () {
	 $uibModalInstance.dismiss('cancel');
	};

	  //--------------------------------------------------------------------------------------------
	  // OAL Converter
	  //-------------------------------------------------------------------------------------------- 
	  $scope.oalMm2Inch= function(  ) {
	     $scope.lot.oalInches = Math.ceil( ($scope.lot.oalmm     / 25.4)  * 1000 ) / 1000 ;
	  }
	 
	  $scope.oalInch2Mm= function(  ) {
		$scope.lot.oalmm     = Math.ceil( ($scope.lot.oalInches * 25.4)  * 100  ) /  100 ;
	  } 

});


//--------------------------------------------------------------------------------------------
//Modal Edit Review Controller
//-------------------------------------------------------------------------------------------- 
wwrApplication.controller('ModalReviewEditCtrl', function ( $scope,
															$rootScope,
														    $uibModalInstance , 
														    $http,
														    $q,
														    ReviewAttachmentService,
														    UserDocumentService ,
														    prec ) {

	$scope.rev = prec;
	$scope.max = 10 ;
	$scope.rev.date = new Date($scope.rev.date) ;

	
	$scope.active = 0 ;
	$scope.myInterval = 2000 ;
	$scope.noWrapSlides = false ;
	
	
	// -------------------------------------------------------------------------------
	//	Prepare Attatchments for post handling
	// -------------------------------------------------------------------------------
	for( i=0 ;  i <  $scope.rev.attachments.length ; i++ ){
		$scope.rev.attachments[i].blobdata   = null  ;    // only for new attachments 
		$scope.rev.attachments[i].sequence   = i ;
	}
	


	
	
	// -------------------------------------------------------------------------------
	//	Save Button Click()
	// -------------------------------------------------------------------------------
	$scope.ok = function () {

		for( i=0 ;  i <  $scope.rev.attachments.length ; i++ ){
			delete $scope.rev.attachments[i].sequence   ;
			delete $scope.rev.attachments[i].blobdata ;
		}
		
	  $scope.deletePicturesFiles() ;

		
	  $uibModalInstance.close($scope.rev);
	};
	
	// -------------------------------------------------------------------------------
	//	Cancel Button Click()
	// -------------------------------------------------------------------------------
	$scope.cancel = function () {
		for( i=0 ; i < $scope.rev.attachments.length ; i++ ){

			if($scope.rev.attachments[i].id == null ){

				var promise = $q.defer();
				
				var udo = {"id": 		$scope.rev.attachments[i].fileId ,
						   "owner": 	$rootScope.ich ,
						   "date": 		new Date()  ,
						   "filename": 	$scope.rev.attachments[i].uploadfilename ,
						   "mediatype": $scope.rev.attachments[i].mediatype } ;
						  
				UserDocumentService.delete(udo)		 
				.$promise
				.then( 	function( result ) { console.log("Temporary UploadFile deleted") ;
				 							 promise.resolve(); }, 
				 		function(errorMsg) { console.log(errorMsg) ;
				 							 promise.reject() ;} 
																	) ;		 		 				
  				
			} ;
		} ;
		
		$uibModalInstance.dismiss('cancel');
	 
	};
	
	// -------------------------------------------------------------------------------
	//	Rating hovering
	// -------------------------------------------------------------------------------
	$scope.hoveringOver = function(value) {
	    $scope.overStar = value;	    
	};	
	
	
	
	
	
	

	// -------------------------------------------------------------------------------
	//	Delete old pictures ()
	// -------------------------------------------------------------------------------
	$scope.deletePicturesFiles = function(){

		
		for( i=0 ; i < $scope.rev.delAttachments.length ; i++ ){	
			var promise = $q.defer();
			ReviewAttachmentService.delete($scope.rev.delAttachments[i])
				.$promise
				.then( 	function( result ) { 
						console.log( "Datei gelöscht") ;
						promise.resolve(); 
					}, 
					function(errorMsg) { promise.reject() ;} 
					) ;
		}
		
	 }

	
	
	
	
	
	
	// -------------------------------------------------------------------------------
	 //	Upload File Change()
	 // -------------------------------------------------------------------------------
	 $scope.fileNameChanged = function (ele) {
	    var files = ele.files;	    
	    var i = 0 ;

	    var loadingImage = loadImage(
            ele.files[i],
            function (canvas) {
            	
            	if (canvas.toBlob) {
            	    canvas.toBlob(
            	        function (blob) {
        	            	
        	            	var fd = new FormData();
        					fd.append('file', blob , files[i].name );			
        		
        					$http.post('rest/secure/upload/reviewdoc' , fd, {
        				        transformRequest: angular.identity,
        				        headers: {'Content-Type': undefined}
        				      })
        				      .success(function( result ){
        				    	  for( j=0 ; j < result.length ; j++){
        					    	  console.log( result[0].uploadfilename + " uploaded")
        	        	              
        					    	  result[0].sequence = $scope.rev.attachments.length ;
        					    	  result[0].blobdata = null ;
        					    	  
        					    	  $scope.rev.attachments.push(result[0]) ;        				    		  
        				    	  }
        				      
        				      })
        				      .error(function(){
        				    	  alert("Upload didn't work: maybe file is to big (max 10MB)");
        		
        				      });
        	            	
        	            	$scope.$apply();
            	        },
            	        'image/jpeg'
            	    );
            	}	            	
            	
        
            },
            { maxWidth: 2048 ,
              orientation: true ,
              canvas: true }
        );
	    
	    
	  } 
	   
	  $scope.deletePicture = function( sequence ){

		  indexObj = -1 ;
		  for( i=0 ; i < $scope.rev.attachments.length ; i++ ){
			  if( $scope.rev.attachments[i].sequence == sequence ){
				  indexObj = i ;
			  }
		  }

		  if( indexObj >= 0 ){
			  if($scope.rev.attachments[indexObj].id != null){
				  $scope.rev.delAttachments.push($scope.rev.attachments[indexObj]) ;
			  }				  
			  $scope.rev.attachments.splice( indexObj , 1) ;
			  for( i=0 ; i < $scope.rev.attachments.length ; i++ ){
				  $scope.rev.attachments[i].sequence = i ;
			  }
		  }

	  }

	 
});


//--------------------------------------------------------------------------------------------
// Modal Delete Controller
//-------------------------------------------------------------------------------------------- 
wwrApplication.controller('RecipeDeleteConfirm', function ($scope, $uibModalInstance , prec ) {

	  $scope.deleteObject = function () {
	    $uibModalInstance.close();
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
	  
}) ;
