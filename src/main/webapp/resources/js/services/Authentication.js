/**
 * Created by mason on 7/10/14.
 */

(function () {
    'use strict';

    angular.module('authentication', [])
        .controller('LoginController', function($scope, $rootScope, AUTH_EVENTS, AuthService){
            $scope.credentials = {
                username: '',
                password: ''
            };

            $scope.login = function(credentials){
                AuthService.login(credentials).then(
                    function (user) {
                        $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);

                    },
                    function(){
                        $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                    }
                );
            };
        })
        .constant('AUTH_EVENTS', {
            loginSuccess: 'authority-login-success',
            loginFailed: 'authority-login-failed',
            logoutSuccess: 'authority-logout-success',
            sessionTimeout: 'authority-session-timeout',
            notAuthenticated: 'authority-not-authenticated',
            notAuthorized: 'authority-not-authorized'
        })
        .constant('USER_ROLES', {
            all: '*',
            admin: 'ADMIN',
            merchant: 'MERCHANT',
            cavalier: 'CAVALIER',
            user: 'USER',
            guest: 'GUEST'
        })
        .factory('AuthService', function($http, Session){
            var authService = {};

            authService.login = function (credentials) {
                return $http
                    .post('/user/login', credentials)
                    .then(function (res) {
                        Session.create(res.id, res.user.id, res.user.role);
                        return res.user;
                    });
            };

            authService.isAuthenticated = function () {
                return !!Session.userId;
            };

            authService.isAuthorized = function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    authorizedRoles = [authorizedRoles];
                }
                return (authService.isAuthenticated() &&
                    authorizedRoles.indexOf(Session.userRole) !== -1);
            };

            return authService;
        })
        .service('Session', function () {
            this.create = function (sessionId, userId, userRole) {
                this.id = sessionId;
                this.userId = userId;
                this.userRole = userRole;
            };
            this.destroy = function () {
                this.id = null;
                this.userId = null;
                this.userRole = null;
            };
            return this;
        })

    ;




})();