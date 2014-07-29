'use strict';

/* Services */

AngularSpringApp.factory('shoppingService', [
    '$http', function($http){
        var baseUrl = '/order';


        var shoppingService = {};

        shoppingService.createOrGetOrder = function(id){
            return $http.get(baseUrl + '/' + id);
        };

        shoppingService.addShoppingItem = function(num, product){
            var item = {};
            var found = false;

            for(var i = 0; i < shoppingService.shoppingCar.length; i++){
                var itemInCar = shoppingService.shoppingCar[i];
                if(itemInCar.product.id == product.id){
                    found = true;
                    item = itemInCar;
                    break;
                }
            }

            if(found){
                item.num = item.num + 1;
            } else {
                item.product = product;
                item.num = num;
                shoppingService.shoppingCar.push(item);
            }
            shoppingService.calculate();
        };

        shoppingService.removeShoppingItem = function(product){
            var item = {};
            var found = false;

            for(var i = 0; i < shoppingService.shoppingCar.length; i++){
                var itemInCar = shoppingService.shoppingCar[i];
                if(itemInCar.product.id == product.id){
                    found = true;
                    item = itemInCar;
                    break;
                }
            }

            if(found){
                shoppingService.shoppingCar.pop(item);
            }
            shoppingService.calculate();
        };

        return shoppingService;
    }
]);

AngularSpringApp.constant('USER_ROLES', {
    all: '*',
    admin: 'ADMIN',
    user: 'USER',
    merchant: 'MERCHANT',
    cavalier: 'CAVALIER'
});

//~===================================
AngularSpringApp.factory('BaseService', ['$http', function($http){
    return {
        loadRefer: function(headers){
            $http.get(headers('Location')).success(function(data){
                this.result = data;
            }).error(function(error){
                alert("Failed to retrieve created merchant " + error.message);
            });
            return this.result;
        }
    };
}]);

AngularSpringApp.factory('Register', ['$http', function ($http) {
    return {
        register:function(data) {
            return $http.post('/account/register', data);
        },
        checkExist: function(username){
            return $http.get('/account/exist?username='+username);
        }
    };
}]);

AngularSpringApp.factory('Session', ['$rootScope', '$cookieStore', 'USER_ROLES', function($rootScope, $cookieStore, USER_ROLES){
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

AngularSpringApp.factory('UserService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/user');
        },
        queryUser: function(query){
            return $http.get('/user/search?query=' + query);
        },
        findById: function(id){
            return $http.get('/user/' + id);
        },
        create: function(data){
            return $http.post('/user', data);
        },
        update: function(id, data){
            return $http.put('/user/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/user/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/user/' + id);
        },
        findAllCavaliers: function(){
            return $http.get('/user/cavalier');
        },
        addRole: function(id, rid){
            return $http.get('/group/' + id + '/role/' + rid);
        },
        removeRole: function(id, rid){
            return $http.delete('/group/' + id + '/role/' + rid);
        }
    };
}]);

AngularSpringApp.factory('GroupService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/group');
        },
        findById: function(id){
            return $http.get('/group/' + id);
        },
        create: function(data){
            return $http.post('/group', data);
        },
        update: function(id, data){
            return $http.put('/group/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/group/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/group/' + id);
        },
        addUser: function(id, uid){
            return $http.get('/group/' + id + '/user/' + uid);
        },
        removeUser: function(id, uid){
            return $http.delete('/group/' + id + '/user/' + uid);
        },
        addRole: function(id, rid){
            return $http.get('/group/' + id + '/role/' + rid);
        },
        removeRole: function(id, rid){
            return $http.delete('/group/' + id + '/role/' + rid);
        }
    };
}]);

AngularSpringApp.factory('RoleService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/role');
        },
        findById: function(id){
            return $http.get('/role/' + id);
        },
        create: function(data){
            return $http.post('/role', data);
        },
        update: function(id, data){
            return $http.put('/role/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/role/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/role/' + id);
        }
    };
}]);

AngularSpringApp.factory('MerchantService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/merchant');
        },
        findMerchantAll: function(){
            return $http.get('/merchant/restaurant');
        },
        findById: function(id){
            return $http.get('/merchant/' + id);
        },
        create: function(data){
            return $http.post('/merchant', data);
        },
        update: function(id, data){
            return $http.put('/merchant/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/merchant/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/merchant/' + id);
        },
        findAllProduct: function(id){
            return $http.get('/merchant/'+ id + '/product');
        },
        createProduct: function(id, data){
            return $http.post('/merchant/'+ id + '/product', data);
        },
        updateProduct: function(id, pid, data){
            return $http.put('/merchant/'+ id + '/product/' + pid, data);
        },
        patchProduct: function(id, pid, data){
            return $http({
                url: '/merchant/'+ id + '/product/' + pid,
                data: data,
                method: 'PATCH'
            });
        },
        removeProduct: function(id, pid){
            return $http.delete('/merchant/'+ id + '/product/' + pid)
        },
        findAllComment: function(id){
            return $http.get('/merchant/'+ id + '/comment');
        }
    };
}]);

AngularSpringApp.factory('CommentService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/comment');
        },
        findById: function(id){
            return $http.get('/comment/' + id);
        },
        create: function(data){
            return $http.post('/comment', data);
        },
        update: function(id, data){
            return $http.put('/comment/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/comment/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/comment/' + id);
        }
    };
}]);

AngularSpringApp.factory('OrderService', ['$http', function($http){
    return {
        orderStatus: function(){
            return $http.get('/order/status');
        },
        findAll: function(query){
            return $http.get('/order' + '?' + query);
        },
        findById: function(id){
            return $http.get('/order/' + id);
        },
        create: function(data){
            return $http.post('/order', data);
        },
        update: function(id, data){
            return $http.put('/order/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/order/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/order/' + id);
        },
        findAllPurchase: function(id){
            return $http.get('/order/'+ id + '/purchase');
        },
        createPurchase: function(id, data){
            return $http.post('/order/'+ id + '/purchase', data);
        },
        updatePurchase: function(id, pid, data){
            return $http.put('/order/'+ id + '/purchase/' + pid, data);
        },
        patchPurchase: function(id, pid, data){
            return $http({
                url: '/order/'+ id + '/purchase/' + pid,
                data: data,
                method: 'PATCH'
            });
        },
        deletePurchase: function(id, pid){
            return $http.patch('/order/'+ id + '/purchase/' + pid);
        },
        queryMerchantOrder: function(id, query){
            return $http.get('/order/merchant/'+ id + '?'+ query);
        },
        queryUserOrder: function(id, query){
            return $http.get('/order/user/'+ id + '?'+ query);
        },
        queryCavalierOrder: function(id, query){
            return $http.get('/order/cavalier/'+ id + '?'+ query);
        },
        findUserOrder: function(query){
            return $http.get('/order/user?'+ query);
        },
        findCavalierOrder: function(query){
            return $http.get('/order/cavalier?'+ query);
        },
        checkout: function(data){
            return $http.post('order/checkout', data);
        }
    };
}]);

AngularSpringApp.factory('ShoppingCarService', ['$rootScope' ,'$http', function($rootScope, $http){
    var shoppingCar = {};

    shoppingCar.clear = function(){
        return $http.delete('/sc');
    };

    shoppingCar.find = function(){
        return $http.get('/sc');
    };

    shoppingCar.addItem = function(product){
        return $http.post('/sc', product);
    };

    shoppingCar.removeItem = function(id){
        return $http.delete('/sc/' + id);
    };

    return shoppingCar;
}]);

AngularSpringApp.factory('ImageService', ['$http', function($http){
    return {
        findAll: function(){
            return $http.get('/image');
        },
        findById: function(id){
            return $http.get('/image/' + id);
        },
        create: function(data){
            return $http.post('/image', data);
        },
        update: function(id, data){
            return $http.put('/image/' + id, data);
        },
        patch: function(id, data){
            return $http({
                url: '/image/' + id,
                data: data,
                method: 'PATCH'
            });
        },
        remove: function(id){
            return $http.delete('/image/' + id);
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
