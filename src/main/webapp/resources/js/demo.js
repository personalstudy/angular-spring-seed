'use strict';

var AngularSpringApp = angular.module('AngularSpringApp', [
    'http-authority-interceptor',
    'AngularSpringAppUtils',
//    'ngResource',
    'ui.bootstrap',
    'ngCookies',
    'ngRoute',
    'ngTouch',
    'mobile-angular-ui'
]);

AngularSpringApp.config(['$locationProvider', '$routeProvider', '$httpProvider', 'USER_ROLES',
    function($locationProvider, $routeProvider, $httpProvider, USER_ROLES){
//        $locationProvider.html5Mode(true);
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
                templateUrl: '/view?vn=public/findpwd',
                controller: 'ResetPasswordController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/', {
                templateUrl: "/view?vn=public/main",
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
            .when('/error', {
                templateUrl: '/view?vn=error',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .otherwise({redirectTo: '/'});

    }]).run(['$rootScope', '$location', 'AuthenticationSharedService', 'Session', 'USER_ROLES',
    function($rootScope, $location, AuthenticationSharedService, Session, USER_ROLES){
        $rootScope.$on('$routeChangeStart', function (event, next) {
            $rootScope.loading = true;
            AuthenticationSharedService.valid(next.access.authorizedRoles);
        });

        $rootScope.$on("$routeChangeSuccess", function(){
            $rootScope.loading = false;
        });

        // Call when the the client is confirmed
        $rootScope.$on('event:authority-loginConfirmed', function() {
            $rootScope.authenticated = true;
            if ($location.path() === '/login') {
                if(Session.hasRole(USER_ROLES.cavalier)){
                    $location.path('/cavalier/current').replace();
                } else {
                    $location.path('/').replace();
                }
            }
        });
        // Call when the 401 response is returned by the server
        $rootScope.$on('event:authority-loginRequired', function(rejection) {
            Session.invalidate();
            $rootScope.authenticated = false;
            if ($location.path() !== '/'
                && $location.path() !== ''
                && $location.path() !== '/register'
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


//  $routeProvider.when('/scroll',    {templateUrl: "scroll.html"});
//  $routeProvider.when('/toggle',    {templateUrl: "toggle.html"});
//  $routeProvider.when('/tabs',      {templateUrl: "tabs.html"});
//  $routeProvider.when('/accordion', {templateUrl: "accordion.html"});
//  $routeProvider.when('/overlay',   {templateUrl: "overlay.html"});
//  $routeProvider.when('/forms',     {templateUrl: "forms.html"});
//  $routeProvider.when('/carousel',  {templateUrl: "carousel.html"});
//
//app.directive( "carouselExampleItem", function($rootScope, $swipe){
//  return function(scope, element, attrs){
//      var startX = null;
//      var startY = null;
//      var endAction = "cancel";
//      var carouselId = element.parent().parent().attr("id");
//
//      var translateAndRotate = function(x, y, z, deg){
//        element[0].style["-webkit-transform"] = "translate3d("+x+"px,"+ y +"px," + z + "px) rotate("+ deg +"deg)";
//        element[0].style["-moz-transform"] = "translate3d("+x+"px," + y +"px," + z + "px) rotate("+ deg +"deg)";
//        element[0].style["-ms-transform"] = "translate3d("+x+"px," + y + "px," + z + "px) rotate("+ deg +"deg)";
//        element[0].style["-o-transform"] = "translate3d("+x+"px," + y  + "px," + z + "px) rotate("+ deg +"deg)";
//        element[0].style["transform"] = "translate3d("+x+"px," + y + "px," + z + "px) rotate("+ deg +"deg)";
//      }
//
//      $swipe.bind(element, {
//        start: function(coords) {
//          endAction = null;
//          startX = coords.x;
//          startY = coords.y;
//        },
//
//        cancel: function(e) {
//          endAction = null;
//          translateAndRotate(0, 0, 0, 0);
//          e.stopPropagation();
//        },
//
//        end: function(coords, e) {
//          if (endAction == "prev") {
//            $rootScope.carouselPrev(carouselId);
//          } else if (endAction == "next") {
//            $rootScope.carouselNext(carouselId);
//          }
//          translateAndRotate(0, 0, 0, 0);
//          e.stopPropagation();
//        },
//
//        move: function(coords) {
//          if( startX != null) {
//            var deltaX = coords.x - startX;
//            var deltaXRatio = deltaX / element[0].clientWidth;
//            if (deltaXRatio > 0.3) {
//              endAction = "next";
//            } else if (deltaXRatio < -0.3){
//              endAction = "prev";
//            } else {
//              endAction = null;
//            }
//            translateAndRotate(deltaXRatio * 200, 0, 0, deltaXRatio * 15);
//          }
//        }
//      });
//    }
//});
//
//app.controller('MainController', function($rootScope, $scope, analytics){
//
//  $rootScope.$on("$routeChangeStart", function(){
//    $rootScope.loading = true;
//  });
//
//  $rootScope.$on("$routeChangeSuccess", function(){
//    $rootScope.loading = false;
//  });
//
//  var scrollItems = [];
//
//  for (var i=1; i<=100; i++) {
//    scrollItems.push("Item " + i);
//  }
//
//  $scope.scrollItems = scrollItems;
//  $scope.invoice = {payed: true};
//
//  $scope.userAgent =  navigator.userAgent;
//  $scope.chatUsers = [
//    { name: "Carlos  Flowers", online: true },
//    { name: "Byron Taylor", online: true },
//    { name: "Jana  Terry", online: true },
//    { name: "Darryl  Stone", online: true },
//    { name: "Fannie  Carlson", online: true },
//    { name: "Holly Nguyen", online: true },
//    { name: "Bill  Chavez", online: true },
//    { name: "Veronica  Maxwell", online: true },
//    { name: "Jessica Webster", online: true },
//    { name: "Jackie  Barton", online: true },
//    { name: "Crystal Drake", online: false },
//    { name: "Milton  Dean", online: false },
//    { name: "Joann Johnston", online: false },
//    { name: "Cora  Vaughn", online: false },
//    { name: "Nina  Briggs", online: false },
//    { name: "Casey Turner", online: false },
//    { name: "Jimmie  Wilson", online: false },
//    { name: "Nathaniel Steele", online: false },
//    { name: "Aubrey  Cole", online: false },
//    { name: "Donnie  Summers", online: false },
//    { name: "Kate  Myers", online: false },
//    { name: "Priscilla Hawkins", online: false },
//    { name: "Joe Barker", online: false },
//    { name: "Lee Norman", online: false },
//    { name: "Ebony Rice", online: false }
//  ];
//
//});