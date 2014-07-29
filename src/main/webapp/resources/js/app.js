'use strict';

var AngularSpringApp =  angular.module('AngularSpringApp', [
    'ui.bootstrap',
    'angularjs-bootstrap-datetimepicker',
    'http-authority-interceptor',
    'AngularSpringAppUtils',
    'truncate',
    'angularFileUpload',
    'ngResource', 'ngRoute', 'ngCookies'
]);

AngularSpringApp.config(['$locationProvider', '$routeProvider', '$httpProvider', 'USER_ROLES',
    function($locationProvider, $routeProvider, $httpProvider, USER_ROLES){
        $locationProvider.hashPrefix('!!!');
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
            .when('/findpassword', {
                templateUrl: '/view?vn=public/findpassword',
                controller: 'ResetPasswordController',
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
            .when('/:id', {
                templateUrl: '/view?vn=public/merchant',
                controller: 'MerchantDetailController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })

            //the user section
            .when('/user/account', {
                templateUrl: '/view?vn=user/account-info',
                controller: 'UInfoController',
                access: {
                    authorizedRoles: [USER_ROLES.user]
                }
            })
            .when('/user/pwd', {
                templateUrl: '/view?vn=user/password',
                controller: 'UChangePasswordController',
                access: {
                    authorizedRoles: [USER_ROLES.user]
                }
            })
            .when('/user/shoppingcar', {
                templateUrl: '/view?vn=user/shopping',
                controller: 'UShoppingCarController',
                access: {
                    authorizedRoles: [USER_ROLES.user]
                }
            })
            .when('/user/order', {
                templateUrl: '/view?vn=user/order',
                controller: 'UOrderController',
                access: {
                    authorizedRoles: [USER_ROLES.user]
                }
            })
            // the cavalier section
            .when('/cavalier/current', {
                templateUrl: '/view?vn=cavalier/in-delivery',
                controller: 'CDeliveryController',
                access: {
                    authorizedRoles: [USER_ROLES.cavalier]
                }
            })
            .when('/cavalier/finished', {
                templateUrl: '/view?vn=cavalier/delivered',
                controller: 'CDeliveredController',
                access: {
                    authorizedRoles: [USER_ROLES.cavalier]
                }
            })

            // the merchant section
            .when('/merchant/merchants', {
                templateUrl: '/view?vn=merchant/merchants',
                controller: 'MRestaurantController',
                access: {
                    authorizedRoles: [USER_ROLES.merchant]
                }
            })
            .when('/merchant/:id', {
                templateUrl: '/view?vn=merchant/merchant',
                controller: 'MerchantDetailController',
                access: {
                    authorizedRoles: [USER_ROLES.merchant]
                }
            })
            .when('/merchant/:id/order', {
                templateUrl: '/view?vn=merchant/orders',
                controller: 'MOrderController',
                access: {
                    authorizedRoles: [USER_ROLES.merchant]
                }
            })
            // the admin section
            .when('/admin/users', {
                templateUrl: '/view?vn=admin/users',
                controller: 'AUserController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/groups', {
                templateUrl: '/view?vn=admin/group',
                controller: 'AGroupController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/roles', {
                templateUrl: '/view?vn=admin/role',
                controller: 'ARoleController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/cavaliers', {
                templateUrl: '/view?vn=admin/cavalier/cavaliers',
                controller: 'ACavalierController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/image', {
                templateUrl: '/view?vn=common/image',
                controller: 'AImageController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/order/query', {
                templateUrl: '/view?vn=admin/order/query',
                controller: 'AOrderQueryController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/order/current', {
                templateUrl: '/view?vn=admin/order/orders',
                controller: 'AOrderController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/merchants', {
                templateUrl: '/view?vn=admin/merchant/merchants',
                controller: 'ARestaurantController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/merchant/:id/products', {
                templateUrl: '/view?vn=admin/merchant/products',
                controller: 'ARestaurantProductController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .when('/admin/merchant/:id/comments', {
                templateUrl: '/view?vn=admin/merchant/comments',
                controller: 'ARestaurantCommentController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
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
                if(Session.hasRole(USER_ROLES.admin)){
                    $location.path('/admin/order/current').replace();
                } else if(Session.hasRole(USER_ROLES.cavalier)){
                    $location.path('/cavalier/current').replace();
                } else if(Session.hasRole(USER_ROLES.merchant)){
                    $location.path('/merchant/merchants').replace();
                } else {
                    $location.path('/').replace();
                }

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


