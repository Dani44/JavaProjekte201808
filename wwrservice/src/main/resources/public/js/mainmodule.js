var wwrApplication = angular.module('wwrApplication', ['ngResource',
                                                       'ngRoute',
                                                       'ngAnimate', 
                                                       'ngTouch',
                                                       'ui.bootstrap',
                                                       'ui.grid' ,
                                                       'ui.grid.autoResize',
                                                       'ui.grid.edit', 
                                                       'ui.grid.saveState',
                                                       'ui.grid.rowEdit', 
                                                       'ui.grid.cellNav',
                                                       'ui.grid.selection',
                                                       'ui.grid.grouping',
                                                       'ui.grid.pinning',
                                                       'ui.grid.resizeColumns',
                                                       'ui.grid.exporter'] ) ;




wwrApplication.config(['$routeProvider',function($routeProvider, $httpProvider) {
	$routeProvider
		.when('/',  			{ templateUrl: 'partials/home.html'		    ,controller: 'navigation'})
		.when('/login',			{ templateUrl: 'partials/login.html'		,controller: 'navigation', controllerAs: 'controller'})
		.when('/recipies',		{ templateUrl: 'partials/recipies.html'		,controller: 'RecipeController'})	
	    .when('/batches',	    { templateUrl: 'partials/batches.html'	    ,controller: 'BatchController'})
	    .when('/reviews',	    { templateUrl: 'partials/reviews.html'	    ,controller: 'ReviewController'})
	    .when('/guns',	        { templateUrl: 'partials/guns.html'	        ,controller: 'GunController'})

	    
	    .otherwise({redirectTo: '/login' });
	
	
  }]) ;



wwrApplication.controller('navigation',

		function($rootScope, $http, $location, $route) {
	
			
	
	

			$http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
				
			var self = this;

			
			
			
			self.tab = function(route) {
				return $route.current && route === $route.current.controller;
			};

			var authenticate = function(credentials, callback) {

				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				$http.get('rest/public/user', {
					headers : headers
				}).then(function(response) {
					if (response.data.name) {
						$rootScope.authenticated = true;
						$rootScope.user = response.data ;

						$http.get("/wwrservice/rest/secure/persons/me")
						.then(function(response) {
							$rootScope.ich = response.data;
						});

						
						
					} else {
						$rootScope.authenticated = false;
						$rootScope.ich = null ;

					}
					
					callback && callback($rootScope.authenticated);

				
				
				
				}, function() {
					$rootScope.authenticated = false;
					$rootScope.ich = null ;

					
					callback && callback(false);
				});
				
				

			}

			authenticate();

			self.credentials = {};
			self.login = function() {
				authenticate(self.credentials, function(authenticated) {
					if (authenticated) {
						console.log("Login succeeded")
						
						
						if( $rootScope.requestedPage != null){
							console.log("Redirect to:" + $rootScope.requestedPage );
							$location.path( $rootScope.requestedPage ) ;
						} else {
							$location.path("recipies");							
						}
						
						
						
						
						
						
						self.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed")
						$location.path("login");
						self.error = true;
						$rootScope.authenticated = false;
					}
				})
			};

			self.logout = function() {
                $http.post('logout', {}).success(function() {
                    $rootScope.authenticated = false;
                    $location.path("login");
                    console.log("Logout succeeded")
                }).error(function(data) {
                    console.log("Logout failed")
                    $rootScope.authenticated = false;
                });         
            }

}) ;


