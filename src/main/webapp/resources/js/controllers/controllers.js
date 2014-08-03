'use strict';

/**
 * Created by mason on 7/1/14.
 */

// ~---- Public ++++++++++++++++++++++++++++++++++++++++++++
AngularSpringApp.controller('LoginController', ['$scope', '$location', 'AuthenticationSharedService',
    function($scope, $location, AuthenticationSharedService){
        $scope.rememberMe = true;

        $scope.login = function(){
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            });
        };
    }
]);
AngularSpringApp.controller('LogoutController', ['$location', 'AuthenticationSharedService',
    function($location, AuthenticationSharedService){
        AuthenticationSharedService.logout();
    }
]);
AngularSpringApp.controller('RegisterController', ['$scope', 'Register',
    function ($scope, Register) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.agreeTerms = false;

        $scope.register = function () {
            alert($scope.account);
            if ($scope.account.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                Register.register($scope.account)
                    .success(function(data){
                        $scope.error = null;
                        $scope.errorUserExists = null;
                        $scope.success = 'OK';
                    })
                    .error(function(data, status, headers){
                        $scope.success = null;
                        if (status === 304 &&
                            data.error && data.error === "Not Modified") {
                            $scope.error = null;
                            $scope.errorUserExists = "ERROR";
                        } else {
                            $scope.error = "ERROR";
                            $scope.errorUserExists = null;
                        }
                    }
                );
            }
        }
    }
]);
AngularSpringApp.controller('HeaderController',['$scope','Session','USER_ROLES',
    function ($scope, Session, USER_ROLES) {
        $scope.init = function(){
            if(!Session.login){
                Session.restoreSession();
            }
            $scope.authenticated = !!Session.login;
            $scope.isAuthorized = Session.isAuthorized;
            $scope.isUser = Session.hasRole(USER_ROLES.user);
            $scope.isMerchant = Session.hasRole(USER_ROLES.merchant);
            $scope.isCavalier = Session.hasRole(USER_ROLES.cavalier);
            $scope.isAdmin = Session.hasRole(USER_ROLES.admin);
        };

        $scope.$on('$routeChangeStart', function (event, next) {
            $scope.authenticated = !!Session.login;
            $scope.isAuthorized = Session.isAuthorized;
            $scope.isUser = Session.hasRole(USER_ROLES.user);
            $scope.isMerchant = Session.hasRole(USER_ROLES.merchant);
            $scope.isCavalier = Session.hasRole(USER_ROLES.cavalier);
            $scope.isAdmin = Session.hasRole(USER_ROLES.admin);
        });
    }
]);
