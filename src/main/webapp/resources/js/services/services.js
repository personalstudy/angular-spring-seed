'use strict';

/* Services */

AngularSpringApp.constant('USER_ROLES', {
    all: '*',
    admin: 'ADMIN',
    user: 'USER'
});

AngularSpringApp.factory('Session', ['$rootScope', '$cookieStore', function($rootScope, $cookieStore){
    var Session = {};
    Session.create = function(data){
        Session.login = data.email;
        Session.user_number = data.user_number;
        Session.setRoles(data.roles);
        $cookieStore.put('cookieAccount', data);
    };
    Session.setRoles=function(roles){
        Session.roles = roles;
    };
    Session.invalidate=function(){
        $cookieStore.remove('cookieAccount');
        Session.login = null;
        Session.user_number = null;
        Session.roles = null;
    };
    Session.hasRole=function(rolename){
        if(Session.roles != null){
            for(var i = 0; i < Session.roles.length; i++){
                var role  = Session.roles[i];
                if(role.name === rolename){
                    return true;
                }
            }
        }
        return false;
    };
    Session.isAuthorized= function (authorizedRoles) {
        if (!angular.isArray(authorizedRoles)) {
            if (authorizedRoles == '*') {
                return true;
            }

            authorizedRoles = [authorizedRoles];
        }

        var isAuthorized = false;
        angular.forEach(authorizedRoles, function(authorizedRole) {
            var authorized = !!Session.login && Session.hasRole(authorizedRole);

            if (authorized || authorizedRole == '*') {
                isAuthorized = true;
            }
        });
        return isAuthorized;
    };
    Session.restoreSession= function(){
        var data = $cookieStore.get('cookieAccount');
        if(!!data){
            Session.login = data.email;
            Session.user_number = data.user_number;
            Session.roles = data.roles;
        } else {
            $rootScope.$broadcast('event:authority-loginRequired');
        }
    };

    return Session;
}]);

AngularSpringApp.factory('AccountService', ['$http', function($http){
    return {
        getMyInfo:function(){
            return $http.get('/account');
        },
        update:function(data){
            return $http.put('/account', data);
        },
        changePassword:function(data){
            return $http.post('/account/password', data);
        },
        remove:function(username){
            return $http.delete('/account/' + username);
        },
        findProfile:function(){
            return $http.get('/account/profile');
        },
        updateProfile:function(data){
            return $http.put('/account/profile', data);
        },
        findRoles:function(){
            return $http.get('/account/role');
        },
        findGroups:function(){
            return $http.get('/account/group');
        },
        findContacts:function(){
            return $http.get('/account/profile/contact');
        },
        createContact:function(data){
            return $http.post('/account/profile/contact', data);
        },
        updateContact:function(id, data){
            return $http.put('/account/profile/contact/' + id, data);
        },
        patchContact:function(id, data){
            return $http({
                url:'/account/profile/contact/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        removeContact: function(id){
            return $http.delete('/account/profile/contact/' + id);
        }
    };
}]);

AngularSpringApp.factory('AuthenticationSharedService', ['$rootScope', '$http', 'authService', 'Session', 'AccountService',
    function ($rootScope, $http, authService, Session, AccountService) {
        return {
            login: function (param) {
                var data ="username=" + param.username +"&password=" + param.password +"&_spring_security_remember_me=" + param.rememberMe +"&submit=Login";
                $http.post('/user/login', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    AccountService.getMyInfo().success(function(data) {
                        Session.create(data);
                        authService.loginConfirmed();
                    }).error(function(data, status, headers, config){
                        $rootScope.authenticationError = true;
                        Session.invalidate();
                    });
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticationError = true;
                    Session.invalidate();
                });

            },
            valid: function (authorizedRoles) {
                $http.get('/account/profile', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        AccountService.getMyInfo(function(data) {
                            Session.create(data);

                            if (!Session.isAuthorized(authorizedRoles)) {
                                event.preventDefault();
                                // user is not allowed
                                $rootScope.$broadcast("event:authority-notAuthorized");
                            }
                            $rootScope.authenticated = true;
                        });
                        }
                        $rootScope.authenticated = !!Session.login;
                    }
                ).error(function (data, status, headers, config) {
                        $rootScope.authenticated = false;
                });
            },
            logout: function () {
                $rootScope.authenticationError = false;
                $rootScope.authenticated = false;
                $rootScope.account = null;

                $http.get('/user/logout');
                Session.invalidate();
                authService.loginCancelled();
            }
        };
    }]);
