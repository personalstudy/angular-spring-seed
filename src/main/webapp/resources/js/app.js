'use strict';

var AngularSpringApp =  angular.module('AngularSpringApp', [
//    'ui.bootstrap',
//    'angularjs-bootstrap-datetimepicker',
    'http-authority-interceptor',
    'AngularSpringAppUtils',
    'truncate',
    'angularFileUpload',
    'ngResource', 'ngRoute', 'ngCookies'
]);

AngularSpringApp.config(['$locationProvider', '$routeProvider', '$httpProvider', 'USER_ROLES',
    function($locationProvider, $routeProvider, $httpProvider, USER_ROLES){
        $httpProvider.defaults.withCredentials = true;

        $routeProvider
            .when('/register', {
                templateUrl: '/view?vn=public/register',
                controller: 'RegisterController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/login', {
                templateUrl: '/view?vn=public/login',
                controller: 'LoginController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/logout', {
                templateUrl: '/view?vn=public/login',
                controller: 'LogoutController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/', {
                templateUrl: '/view?vn=public/main',
                controller: 'MainController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/error', {
                templateUrl: '/view?vn=error',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .otherwise({redirectTo: '/'});

}]).run(['$rootScope', '$location', '$http', 'AuthenticationSharedService', 'Session', 'USER_ROLES',
    function($rootScope, $location, $http, AuthenticationSharedService, Session, USER_ROLES){
        $rootScope.$on('$routeChangeStart', function (event, next) {
            AuthenticationSharedService.valid(next.access.authorizedRoles);
        });
        // Call when the the client is confirmed
        $rootScope.$on('event:authority-loginConfirmed', function() {
            $rootScope.authenticated = true;
            if ($location.path() === "/login") {
                $location.path('/').replace();
            }
        });
        // Call when the 401 response is returned by the server
        $rootScope.$on('event:authority-loginRequired', function(rejection) {
            Session.invalidate();
            $rootScope.authenticated = false;
            if ($location.path() !== "/"
                && $location.path() !== ""
                && $location.path() !== "/register"
                // && $location.path() !== "/activate"
                ) {
                $location.path('/login').replace();
            }
        });
        // Call when the 403 response is returned by the server
        $rootScope.$on('event:authority-notAuthorized', function(rejection) {
            $rootScope.errorMessage = 'errors.403';
            $location.path('/error').replace();
        });
        // Call when the user logs out
        $rootScope.$on('event:authority-loginCancelled', function() {
            $location.path('');
        });
    }
]);


