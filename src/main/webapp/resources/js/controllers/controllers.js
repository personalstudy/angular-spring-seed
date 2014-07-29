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
//                Register.save($scope.register,
//                    function (value, responseHeaders) {
//                        $scope.error = null;
//                        $scope.errorUserExists = null;
//                        $scope.success = 'OK';
//                    },
//                    function (httpResponse) {
//                        $scope.success = null;
//                        if (httpResponse.status === 304 &&
//                            httpResponse.data.error && httpResponse.data.error === "Not Modified") {
//                            $scope.error = null;
//                            $scope.errorUserExists = "ERROR";
//                        } else {
//                            $scope.error = "ERROR";
//                            $scope.errorUserExists = null;
//                        }
//                    }
//                );
            }
        }
    }
]);
AngularSpringApp.controller('ResetPasswordController', ['$scope', 'AccountService',
    function($scope, AccountService){
        $scope.find = {};
        $scope.sendEmail = function(){
            if(!$scope.find || !$scope.find.email){
                alert('请输入您的邮箱地址！');
            } else {
                //TODO
            }
        }
    }
]);
AngularSpringApp.controller('MerchantDetailController', ['$scope', '$routeParams', '$modal', 'MerchantService', 'BaseService', 'ShoppingCarService', 'AuthenticationSharedService',
    function($scope, $routeParams, $modal, MerchantService, BaseService, ShoppingCarService, AuthenticationSharedService){
        $scope.findMerchant = function(){
            MerchantService.findById($routeParams.id).success(
                function(data, status, headers, config){
                    $scope.merchant = data;
                }
            );
        };

        $scope.findAllProduct = function(){
            MerchantService.findAllProduct($routeParams.id).success(
                function(data, status, headers, config){
                    $scope.products = data;
                }
            );
        };

        $scope.findAllComment = function(){
            MerchantService.findAllComment($routeParams.id).success(
                function(data, status, headers, config){
                    $scope.comments = data;
                }
            );
        };

        $scope.addItem = function(product){
            alert("add");
            ShoppingCarService.addItem({number: 1, product: product }).success(
                function(data, status, headers, config){
                    $scope.find();
                }
            );
        };

        $scope.findMerchant();
        $scope.findAllProduct();
        $scope.findAllComment();
    }
]);
AngularSpringApp.controller('MainController', ['$scope', 'MerchantService', 'Session',
    function ($scope, MerchantService, Session) {
        $scope.merchants = [];
        $scope.merchant = {};
        $scope.editMode = false;

        $scope.loadAll = function(){
            MerchantService.findAll().success(
                function(data, status, headers, config){
                    for(var i = 0; i < data.length; i++){
                        data[i].rate = 4;
                    }
                    $scope.merchants = data;

                }
            );
        };

        $scope.loadAll();
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

// ~---- User ++++++++++++++++++++++++++++++++++++++++++++
AngularSpringApp.controller('UChangePasswordController', ['$scope', 'AccountService',
    function ($scope, AccountService) {
        $scope.success = null;

        $scope.init = function(){
            $scope.password = null;
            $scope.confirmPassword = null;
            $scope.oldPassword = null;
        };

        $scope.changePassword = function () {
            if ($scope.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                AccountService.changePassword(
                    {
                        newpwd:$scope.password,
                        oldpwd:$scope.oldPassword
                    }
                ).success(function(data, status, headers, config){
                    if(status == 202){
                        $scope.success = '修改成功！';
                    }
                }).error(function(data, status, headers, config){
                    $scope.error = 'ERROR';
                });
                $scope.init();
            }
        };
}]);
AngularSpringApp.controller('UInfoController', ['$scope', 'AccountService',
    function($scope, AccountService){
        $scope.profileEdit = false;
        $scope.contactEdit = false;
        $scope.edit = false;

        $scope.getAccountInfo = function(){
            AccountService.getMyInfo().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.accountInfo = data;
                    }
                }
            );
        };

        $scope.editProfile = function(){
            AccountService.findProfile().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.profile = data;
                        $scope.profileEdit = true;
                    }
                }
            );
        };

        $scope.updateProfile = function(data){
            AccountService.updateProfile(data).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.getAccountInfo();
                        $scope.profileEdit = false;
                    }
                }
            );
        };

        $scope.myContacts = function(){
            AccountService.findContacts().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.contacts = data;
                    }
                }
            );
        };

        $scope.createContact = function(data){
            AccountService.createContact(data).success(
                function(data, status, headers, config){
                    if(status == 201){
                        $scope.myContacts();
                    }
                }
            );
            $scope.contactEdit = false;
        };

        $scope.addContact = function(){
            $scope.contactEdit = true;
        };

        $scope.editContact = function(contact){
            $scope.contact = contact;
            $scope.contactEdit = true;
            $scope.edit = true;
        };

        $scope.updateContact = function(id, data){
            AccountService.patchContact(id, data).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.myContacts();
                        $scope.contactEdit = false;
                        $scope.edit = false;
                    }
                }
            );
            $scope.contactEdit = false;
            $scope.edit = false;
        };

        $scope.deleteContact = function(id){
            AccountService.removeContact(id).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.myContacts();
                    }
                }
            );
        };


        $scope.myContacts();
        $scope.getAccountInfo();
}]);
AngularSpringApp.controller('UShoppingCarController', ['$scope', 'ShoppingCarService', 'AccountService','OrderService',
    function($scope, ShoppingCarService, AccountService, OrderService){
        $scope.checkoutModel = {};


        //DATE AND TIME
        $scope.today = function() {
            $scope.meatDate = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.meatDate = null;
        };

        $scope.toggleMin = function() {
            var now = new Date();
            var maxDate = new Date();

            maxDate.setTime(maxDate.getTime() + 7*24*3600*1000);
            $scope.minDate = $scope.minDate ? null : now;
            $scope.maxDate = $scope.maxDate ? null : maxDate;
        };
        $scope.toggleMin();

        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.format = 'yyyy-MM-dd';



        $scope.clear = function(product){
            ShoppingCarService.addItem(product).success(
                function(data, status, headers, config){
                    $scope.shoppingCar = [];
                }
            );
        };
        $scope.find = function(){
            ShoppingCarService.find().success(
                function(data, status, headers, config){
                    $scope.shoppingCar = data;
                }
            );
        };
        $scope.addItem = function(product){
            alert("add");
            ShoppingCarService.addItem({number: 1, product: product }).success(
                function(data, status, headers, config){
                    $scope.find();
                }
            );
        };
        $scope.minusItem = function(product){
            ShoppingCarService.addItem({number: -1, product: product }).success(
                function(data, status, headers, config){
                    $scope.find();
                }
            );
        };
        $scope.removeItem = function(id){
            ShoppingCarService.removeItem(id).success(
                function(data, status, headers, config){
                    $scope.find();
                }
            )
        };
        $scope.countShoppingCarItem = function(items){
            if(items == null){
                return 0;
            }
            var number = 0;
            for(var i = 0; i < items.length; i++){
                number += items[i].number;
            }
            return number;
        };
        $scope.countShoppingCarPrice = function(items){
            if(items == null){
                return 0;
            }
            var number = 0;
            for(var i = 0; i < items.length; i++){
                number += items[i].number * items[i].product.price;
            }
            return number;
        };

        $scope.myContacts = function(){
            AccountService.findContacts().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.contacts = data;
                    }
                }
            );
        };

        $scope.setContact = function(data){
            if($scope.contact == data){
                $scope.contact = null;
            } else {
                $scope.contact = data;
            }
            $scope.checkEnableCheckout();
        };

        $scope.checkout = function(){
            $scope.checkEnableCheckout();

            var cmodel = {};
            $scope.buildMeatTime();

            cmodel.meet_time = $scope.meet_time;

            cmodel.addition_info = $scope.addition_info;
            if(cmodel.addition_info === undefined){
                cmodel.addition_info = '';
            }

            cmodel.contact = $scope.contact;
            if(cmodel.contact === undefined){
                alert("请选择或输入送餐地址");
                return;
            }
            OrderService.checkout(cmodel).success(
                function(data, status, headers, config){
                    $scope.enableCheckout = false;
                    $scope.checkoutModel = {};
                    $scope.myContacts();
                    $scope.find();
                    $scope.checkOuted = true;
                }
            );
        };

        $scope.buildMeatTime = function(){
            if(!$scope.meatDate){
                alert("请选择用餐日期");
                return;
            }
            var minMeetTime = new Date();
            minMeetTime.setTime(minMeetTime.getTime() + 1 * 3600 * 1000);
            if($scope.meatDate < minMeetTime){
                alert("最早用餐时间不能少于一个小时！");
                return;
            } else{
                $scope.meet_time = $scope.meatDate;
            }
        };

        $scope.checkEnableCheckout = function(){
            if( $scope.shoppingCar &&
                $scope.shoppingCar.items &&
                $scope.shoppingCar.items.length > 0){

                if($scope.contact &&
                    $scope.contact.id != null){
                    $scope.enableCheckout = true;
                } else {
                    if($scope.contact &&
                        $scope.contact.receipt != null
                        && $scope.contact.address != null
                        && $scope.contact.phone != null){
                        $scope.enableCheckout = true;
                    } else {
                        $scope.enableCheckout = false;
                    }
                }
            } else {
                $scope.enableCheckout = false;
            }
        };

        $scope.myContacts();
        $scope.find();
        $scope.checkEnableCheckout();
    }
]);
AngularSpringApp.controller('UOrderController', ['$scope', 'OrderService',
    function($scope, OrderService){
        $scope.status;
        $scope.from;
        $scope.to;

        $scope.open = function($event, order) {
            $event.preventDefault();
            $event.stopPropagation();

            if(order == 0){
                $scope.fromOpened = true;
            } else if(order == 1){
                $scope.toOpened = true;
            }
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.format = 'yyyy-MM-dd';

        $scope.initStatus = function(){
            $scope.noContent = false;
            $scope.noQueryResult = false;
        };

        $scope.currentOrders = function(status){
            $scope.initStatus();
            var query = 'status=' + status;
            OrderService.findUserOrder(query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status == 204){
                        $scope.results = [];
                        $scope.noContent = true;
                    }
                }
            );
        };
        $scope.queryOrder = function(){
            $scope.initStatus();
            var query = '';
            if($scope.status){
                query += 'status=';
                query += $scope.status;
            }
            if($scope.from){
                query += '&from=';
                query += $scope.from.toISOString();

            }
            if($scope.to){
                query += '&to=';
                query += $scope.to.toISOString();
            }

            if(query == ''){
                alert("请输入过滤条件!")
            } else {
                OrderService.findUserOrder(query).success(
                    function(data, status, headers, config){
                        if(status == 200){
                            $scope.results = data;
                        } else if(status == 204){
                            $scope.results = [];
                            $scope.noQueryResult = true;
                        }
                    }
                );
            }
        };
        $scope.statusList = [];
        $scope.loadAllStatus = function(){
            OrderService.orderStatus().success(function(data, status, headers, config){
                if(status == 200){
                    $scope.statusList = data;
                }
            });
        };

        $scope.loadAllStatus();
}]);

// ~----- Cavalier ++++++++++++++++++++++++++++++++++++++++++
AngularSpringApp.controller('CDeliveryController', ['$scope', 'OrderService',
    function($scope, OrderService){
        $scope.type;

        $scope.initStatus = function(){
            $scope.noQueryResult = false;
        };

        $scope.currentOrders = function(status){
            $scope.initStatus();
            var query = '';
            if(status){
                query += 'status=';
                query += status;
            }

            OrderService.findCavalierOrder(query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if (status = 204){
                        $scope.results = [];
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

        $scope.reload = function(){
            $scope.currentOrders();
        };

        $scope.ordersIndevilery = function(){
            $scope.type = 'ON_THE_WAY';
            $scope.currentOrders('ON_THE_WAY');
        };
        $scope.ordersNeedToDeliver = function(){
            $scope.type = 'DELIVERED';
            $scope.currentOrders('DELIVERED');
        };

        $scope.finishOrder = function(id){
            OrderService.patch(id, {order_status:'FINISHED'}).success(
                function(data, status, headers,config){
                    $scope.ordersIndevilery();
                }
            );
        };

        $scope.getOrder = function(id){
            OrderService.patch(id, {order_status:'ON_THE_WAY'}).success(
                function(data, status, headers,config){
                    $scope.ordersNeedToDeliver();
                }
            );

        };

        $scope.ordersIndevilery();
    }

]);
AngularSpringApp.controller('CDeliveredController', ['$scope', 'OrderService',
    function($scope, OrderService){
        $scope.from;
        $scope.to;

        $scope.open = function($event, order) {
            $event.preventDefault();
            $event.stopPropagation();

            if(order == 0){
                $scope.fromOpened = true;
            } else if(order == 1){
                $scope.toOpened = true;
            }
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.format = 'yyyy-MM-dd';

        $scope.initStatus = function(){
            $scope.noQueryResult = false;
        };

        $scope.queryFinishedOrders = function(from, to){
            $scope.initStatus();
            var query = 'status=FINISHED';

            if($scope.from){
                query += '&from=';
                query += $scope.from.toISOString();

            }
            if($scope.to){
                query += '&to=';
                query += $scope.to.toISOString();
            }
            OrderService.findCavalierOrder(query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status == 204){
                        $scope.results = [];
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

//        $scope.queryFinishedOrders();
    }
]);

// ~----- Merchant +++++++++++++++++++++++++++++++++++++++++++++
AngularSpringApp.controller('MRestaurantController', ['$scope', 'MerchantService',
    function($scope, MerchantService){
        $scope.findAll = function(){
            MerchantService.findMerchantAll().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.merchants = data;
                    }
                }
            );
        };

        $scope.findAll();
    }
]);
AngularSpringApp.controller('MOrderController', ['$scope', '$routeParams', 'OrderService',
    function($scope, $routeParams, OrderService){
        $scope.restaurantId = $routeParams.id;

        $scope.from;
        $scope.to;

        $scope.initStatus = function(){
            $scope.noQueryResult = false;
        };

        $scope.findNewOrders = function(){
            var query = 'status=NOTIFIED,DELIVERED';

            OrderService.queryMerchantOrder($scope.restaurantId, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status = 204){
                        $scope.results = data;
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

        $scope.findUnfinishedOrders = function(){
            var query = 'status=ON_THE_WAY';

            OrderService.queryMerchantOrder($scope.restaurantId, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status = 204){
                        $scope.results = data;
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

        $scope.findFinishedOrders = function(){
            var query = 'status=FINISHED';

            if($scope.from){
                query += '&from=';
                query += $scope.from.toISOString();

            }
            if($scope.to){
                query += '&to=';
                query += $scope.to.toISOString();
            }

            OrderService.queryMerchantOrder($scope.restaurantId, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status = 204){
                        $scope.results = data;
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

        $scope.findNewOrders();
    }
]);

// ~------- Admin +++++++++++++++++++++++++++++++++++++++++++++++
AngularSpringApp.controller('ARestaurantController', ['$scope', 'MerchantService',
    function($scope, MerchantService){
        $scope.createOrUpdate = false;
        $scope.edit = false;

        $scope.findAll = function(){
            MerchantService.findAll().success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.merchants = data;
                    }
                }
            );
        };

        $scope.create = function(data){
            alert(JSON.stringify(data));
            MerchantService.create(data).success(
                function(data, status, headers, config){

                    if(status == 201){
                        $scope.findAll();
                        $scope.createOrUpdate = false;
                    }
                }
            );
        };

        $scope.update = function(id, data){
            MerchantService.update(id, data).success(function(data, status, headers, config){
                if(status == 200){
                    $scope.findAll();
                    $scope.createOrUpdate = false;
                    $scope.edit = false;
                }
            });
        };

        $scope.remove = function(id){
            MerchantService.remove(id).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.findAll();
                    }
                }
            );
        };

        $scope.addMerchant = function(){
            $scope.createOrUpdate = true;
        };

        $scope.editMerchant = function(data){
            $scope.merchant = data;
            $scope.createOrUpdate = true;
            $scope.edit = true;
        };

        $scope.reset = function(){
            $scope.createOrUpdate = false;
            $scope.edit = false;
            $scope.merchant = null;
        };

        $scope.findAll();
    }
]);
AngularSpringApp.controller('ARestaurantProductController', ['$scope', '$routeParams', 'MerchantService',
    function($scope, $routeParams, MerchantService){
        $scope.merchantId = $routeParams.id;

        $scope.findAllProduct = function(){
            MerchantService.findAllProduct($scope.merchantId).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.products = data;
                    }
                }
            );
        };

        $scope.create = function(data){
            MerchantService.createProduct($scope.merchantId, data).success(
                function(data, status, headers, config){
                    if(status == 201){
                        $scope.findAllProduct();
                        $scope.createOrUpdate = false;
                    }
                }
            );
        };

        $scope.update = function( pid, data){
            MerchantService.updateProduct($scope.merchantId, pid, data).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.findAllProduct();
                        $scope.createOrUpdate = false;
                        $scope.edit = false;
                    }
                }
            );
        };

        $scope.removeProduct = function(pid){
            MerchantService.removeProduct($scope.merchantId, pid).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.findAllProduct();
                    }
                }
            );
        };

        $scope.addProduct = function(){
            $scope.createOrUpdate = true;
        };

        $scope.editProduct = function(data){
            $scope.product = data;
            $scope.createOrUpdate = true;
            $scope.edit = true;
        };

        $scope.reset = function(){
            $scope.createOrUpdate = false;
            $scope.edit = false;
            $scope.product = null;
        };

        $scope.findAllProduct();
    }
]);
AngularSpringApp.controller('ARestaurantCommentController', ['$scope', '$routeParams', 'MerchantService',
    function($scope, $routeParams, MerchantService){
        $scope.merchantId = $routeParams.id;

        $scope.findAllComment = function(){
            MerchantService.findAllComment($scope.merchantId).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.comments = data;
                    }
                }
            );
        };



        $scope.findAllComment();
    }
]);
AngularSpringApp.controller('ACavalierController', ['$scope', 'UserService',
    function($scope, UserService){
        $scope.findAllCavaliers = function(){
            UserService.findAllCavaliers().success(function(data, status, headers, config){
               if(status == 200){
                   $scope.cavaliers = data;
               }
            });
        };

        $scope.createCavalier = function (data) {
            UserService.create(data).success(
                function(data, status, headers, config){
                    if(status == 201) {
                        $scope.findAllCavaliers();
                    }
                }
            );
        };

        $scope.findAllCavaliers();
    }
]);
AngularSpringApp.controller('AOrderController', ['$scope', 'OrderService', 'UserService',
    function($scope, OrderService, UserService){
        $scope.type;
        $scope.cavalier_user_id;

        $scope.status;
        $scope.from;
        $scope.to;

        $scope.initStatus = function(){
            $scope.noQueryResult = false;
        };

        $scope.findOrderWithStatus = function(status){
            $scope.initStatus();
            var query = '';

            if(status){
                $scope.status = status;
                query += 'status=';
                query += status;
            }

            if($scope.from){
                query += '&from=';
                query += $scope.from.toISOString();
            }
            if($scope.to){
                query += '&to=';
                query += $scope.to.toISOString();
            }

            OrderService.findAll(query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    } else if(status == 204){
                        $scope.results = [];
                        $scope.noQueryResult = true;
                    }
                }
            );
        };

        $scope.findAllCavaliers = function(){
            UserService.findAllCavaliers().success(function(data, status, headers, config){
                if(status == 200){
                    $scope.cavaliers = data;
                }
            });
        };

        $scope.findOrderOrdered = function(){
            $scope.type = 'ORDERED';
            $scope.findOrderWithStatus('ORDERED');
        };
        $scope.findOrderNotified = function(){
            $scope.type = 'NOTIFIED';
            $scope.findOrderWithStatus('NOTIFIED');
        };
        $scope.findOrderDelivered = function(){
            $scope.type = 'DELIVERED';
            $scope.findOrderWithStatus('DELIVERED');
        };
        $scope.findOrderOnTheWay = function(){
            $scope.type = 'ON_THE_WAY';
            $scope.findOrderWithStatus('ON_THE_WAY');
        };

        $scope.confirmAssign = function(id){
            alert($scope.cavalier_user_id);
            alert(JSON.stringify($scope.cavaliers));
            if($scope.cavalier_user_id == null){
                alert("请先选择骑士！");
            } else {
                OrderService.patch(id, {cavalier:{id: $scope.cavalier_user_id}}).success(
                    function(data, status, headers, config){
                        $scope.refresh();
                        $scope.cavalier_user_id = null;
                    }
                );
                $scope.showAssign = false;
            }
        };
        $scope.cancelAssign = function(){
            $scope.order = null;
            $scope.cavalier_user_id = null;
            $scope.showAssign = false;
        };

        $scope.assignDelivery = function(data){
            $scope.order = data;
            $scope.showAssign = true;
        };

        $scope.sendToMerchant = function(id){
            if($scope.addition_info == null){
                $scope.addition_info = "";
            }

            OrderService.patch(id, {order_status:'NOTIFIED', addition_info: $scope.addition_info }).success(
                function(data, status, headers, config){
                    $scope.refresh();
                    $scope.addition_info = null;
                }
            );

        };

        $scope.refresh = function(){
            $scope.findOrderWithStatus($scope.status);
            $scope.findAllCavaliers();
        };

        $scope.findAllCavaliers();
        $scope.findOrderOrdered();
    }
]);
AngularSpringApp.controller('AOrderQueryController', ['$scope', 'OrderService', 'UserService',
    function($scope, OrderService, UserService){
        $scope.type = 'C';
        $scope.queryId;
        $scope.status;
        $scope.from;
        $scope.to;

        $scope.buildQuery = function(){
            var query = '';
            if($scope.status){
                query += 'status=';
                query += $scope.status;
            }

            if($scope.from){
                query += '&from=';
                query += $scope.from.toISOString();

            }
            if($scope.to){
                query += '&to=';
                query += $scope.to.toISOString();
            }

            return query;
        };

        $scope.queryMerchantOrder = function(id){
            var query = $scope.buildQuery();
            OrderService.queryMerchantOrder(id, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    }
                }
            )
        };
        $scope.queryUserOrder =  function(id){
            var query = $scope.buildQuery();
            OrderService.queryUserOrder(id, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    }
                }
            )
        };
        $scope.queryCavalierOrder = function(id){
            var query = $scope.buildQuery();
            OrderService.queryCavalierOrder(id, query).success(
                function(data, status, headers, config){
                    if(status == 200){
                        $scope.results = data;
                    }
                }
            )
        };

        $scope.setQueryType = function(type){
            $scope.type = type;
        };

        $scope.query = function(){
            if($scope.type == 'C'){
                $scope.queryCavalierOrder($scope.queryId);
            } else if($scope.type == 'U'){
                $scope.queryUserOrder($scope.queryId);
            }
            else if($scope.type == 'M'){
                $scope.queryMerchantOrder($scope.queryId);
            } else {
                $scope.queryCavalierOrder($scope.queryId);
            }
        };

        $scope.statusList = [];
        $scope.loadAllStatus = function(){
            OrderService.orderStatus().success(function(data, status, headers, config){
                if(status == 200){
                    $scope.statusList = data;
                }
            });
        };

        $scope.loadAllStatus();
    }
]);
AngularSpringApp.controller('AUserController', ['$scope', 'RoleService', 'GroupService', 'UserService',
    function($scope, RoleService, GroupService, UserService){

        $scope.findAll = function(){
            UserService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.users = data;
                    } else if (status == 201 ){
                        alert("No User Found");
                    }
                }
            );
        };

        $scope.queryUser = function(queryKey){
            $scope.loading = true;

            UserService.queryUser(queryKey)
                .then(function(res){
                    $scope.users = res.data;
//                    return res.data;
                });

            $scope.loading = false;
        };

        $scope.findById = function(id){
            UserService.findById(id).success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.user = data;
                    } else if (status == 201 ){
                        alert("No User Found");
                    }
                }
            );
        };
        $scope.findByUsername = function(username){
            UserService.findByUsername(username).success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.user = data;
                        $scope.users = [];
                        $scope.users.push($scope.user);
                        $scope.user = null;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };

        $scope.create = function (data) {
            UserService.create(data).success(
                function(data, status, headers, config){
                    if(status == 201) {
                        $scope.findByUsername(data.email);
                    }
                }
            );
        };

        $scope.update = function (id, data) {
            UserService.patch(id, data).success(
                function(data, status, headers, config){
                    if(status == 20) {
                        $scope.findByUsername(data.email);
                    }
                }
            );
        };

        $scope.remove = function (id) {
            UserService.remove(id).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.user = {};
                    }
                }
            );
        };

        $scope.findAllRoles = function(){
            RoleService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.roles = data;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };

        $scope.findAllGroups = function(){
            GroupService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.groups = data;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };

        $scope.addRoleToUser = function (id, rid) {
            UserService.addRole(id, rid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findById(id);
                    }
                }
            );
        };
        $scope.removeRoleFromUser = function (id, rid) {
            UserService.remove(id, rid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findById(id);
                    }
                }
            );
        };

        $scope.addUserToGroup = function (gid, uid) {
            GroupService.addUser(gid, uid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findById(uid);
                    }
                }
            );
        };
        $scope.removeUserFromGroup = function (gid, uid) {
            GroupService.removeUser(gid, uid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findById(uid);
                    }
                }
            );
        };

        $scope.addUser = function(){
            $scope.createOrUpdate = true;
        };

        $scope.editUser = function(data){
//            $scope.edit = true;
        };

        $scope.addGroup = function(uid){
            $scope.user_id = uid;
            $scope.groupAdd = true;
        };

        $scope.addRole = function(uid){
            $scope.user_id = uid;
            $scope.roleAdd = true;
        };

        $scope.reset = function(){
            $scope.createOrUpdate = false;
            $scope.edit = false;
            $scope.user = null;
            $scope.user_id = null;
            $scope.groupAdd = false;
            $scope.roleAdd = false;
        };

//        $scope.findAll();
        $scope.findAllRoles();
        $scope.findAllGroups();
    }
]);
AngularSpringApp.controller('ARoleController', ['$scope','RoleService',
    function($scope, RoleService){
        $scope.findAll = function(){
            RoleService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.roles = data;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };

        $scope.create = function (data) {
            RoleService.create(data).success(
                function(data, status, headers, config){
                    if(status == 201) {
                        $scope.findAll();
                    }
                }
            );
        };

        $scope.remove = function (id) {
            RoleService.remove(id).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findAll();
                    }
                }
            );
        };


        $scope.addRole = function(){
            $scope.createOrUpdate = true;
        };

        $scope.reset = function(){
            $scope.createOrUpdate = false;
            $scope.role = null;
        };

        $scope.findAll();
    }
]);
AngularSpringApp.controller('AGroupController', ['$scope','GroupService', 'RoleService',
    function($scope, GroupService, RoleService){
        $scope.findAll = function(){
            GroupService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.groups = data;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };
        $scope.findAllRoles = function(){
            RoleService.findAll().success(
                function (data, status, headers, config) {
                    if(status == 200){
                        $scope.roles = data;
                    } else if (status == 201 ){
                        alert("No Groups Found");
                    }
                }
            );
        };

        $scope.create = function (data) {
            GroupService.create(data).success(
                function(data, status, headers, config){
                    if(status == 201) {
                        $scope.findAll();
                    }
                }
            );
        };
        $scope.remove = function (id) {
            GroupService.remove(id).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findAll();
                    }
                }
            );
        };
        $scope.addRoleToGroup = function (id, rid) {
            GroupService.addRole(id, rid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findAll();
                        $scope.roleAdd = false;
                    }
                }
            );
        };
        $scope.removeRoleFromGroup = function (id, rid) {
            GroupService.removeRole(id, rid).success(
                function(data, status, headers, config){
                    if(status == 200) {
                        $scope.findAll();
                    }
                }
            );
        };


        $scope.addGroup = function(){
            $scope.createOrUpdate = true;
        };

        $scope.reset = function(){
            $scope.createOrUpdate = false;
            $scope.roleAdd = false;
            $scope.group = null;
            $scope.group_role_id = null;
        };

        $scope.addRole = function(id){
            $scope.roleAdd = true;
            $scope.group_id = id;
        }

        $scope.findAll();
        $scope.findAllRoles();
    }
]);
AngularSpringApp.controller('AImageController', ['$scope', 'ImageService','$timeout', '$upload',
    function ($scope, ImageService, $timeout, $upload) {
        $scope.success = null;
        $scope.error = null;

        $scope.findAll = function(){
            ImageService.findAll().success(
                function(data, status, headers, config){
                    $scope.images = data;
                }
            )
        };

        $scope.editMode = false;

        $scope.changeMode = function(){
            if($scope.editMode){
                $scope.editMode = false;
            } else {
                $scope.editMode = true;
            }
        };

        $scope.findAll();

        $scope.remove = function (id) {
            ImageService.remove(id).success(function () {
                $scope.findAll();
            }).error(function () {
                $scope.setError('Could not remove Image');
            });
        };

        //~ upload image ==================
        $scope.fileReaderSupported = window.FileReader != null;

        $scope.hasUploader = function(index){
            return $scope.upload[index] != null;
        };

        $scope.abort = function(index) {
            $scope.upload[index].abort();
            $scope.upload[index] = null;
        };

        $scope.onFileSelect = function($files) {
            $scope.selectedFiles = [];
            $scope.progress = [];
            if ($scope.upload && $scope.upload.length > 0) {
                for (var i = 0; i < $scope.upload.length; i++) {
                    if ($scope.upload[i] != null) {
                        $scope.upload[i].abort();
                    }
                }
            }
            $scope.upload = [];
            $scope.uploadResult = [];
            $scope.selectedFiles = $files;
            $scope.dataUrls = [];
            for ( var i = 0; i < $files.length; i++) {
                var $file = $files[i];
                if (window.FileReader && $file.type.indexOf('image') > -1) {
                    var fileReader = new FileReader();
                    fileReader.readAsDataURL($files[i]);
                    var loadFile = function(fileReader, index) {
                        fileReader.onload = function(e) {
                            $timeout(function() {
                                $scope.dataUrls[index] = e.target.result;
                            });
                        }
                    }(fileReader, i);
                }
                $scope.progress[i] = -1;

                $scope.start(i);
            }
        };
        $scope.start = function(index) {
            $scope.progress[index] = 0;
            $scope.errorMsg = null;
            $scope.upload[index] = $upload.upload({
                url : 'image/upload',
                method: 'POST',
                file: $scope.selectedFiles[index],
                fileFormDataName: 'attachment'
            }).then(function(response) {
                $scope.uploadResult.push(response.data);
            }, function(response) {
                if (response.status > 0) $scope.errorMsg = response.status + ': ' + response.data;
            }, function(evt) {
                // Math.min is to fix IE which reports 200% sometimes
                $scope.progress[index] = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            }).xhr(function(xhr){
                xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
            });
        };
    }
]);

// ~ duplicated
//AngularSpringApp.controller('PasswordController', ['$scope', 'Password',
//    function ($scope, Password) {
//        $scope.success = null;
//        $scope.error = null;
//        $scope.doNotMatch = null;
//
//        $scope.changePassword = function () {
//            if ($scope.password != $scope.confirmPassword) {
//                $scope.doNotMatch = "ERROR";
//            } else {
//                $scope.doNotMatch = null;
//                Password.save($scope.password,
//                    function (value, responseHeaders) {
//                        $scope.error = null;
//                        $scope.success = 'OK';
//                    },
//                    function (httpResponse) {
//                        $scope.success = null;
//                        $scope.error = "ERROR";
//                    });
//            }
//        };
//    }]);
//
//AngularSpringApp.controller('MainController', ['$scope', 'MerchantService',
//    function ($scope, MerchantService) {
//        $scope.merchants = [];
//        $scope.merchant = {};
//        $scope.editMode = false;
//
//        $scope.loadAll = function(){
//            MerchantService.findAll().success(
//                function(data, status, headers, config){
//                    $scope.merchants = data;
//                }
//            );
//        };
//
//        $scope.loadAll();
//
//    }
//]);
//
//AngularSpringApp.controller('MerchantController', ['$scope', 'ImageService', 'MerchantService', 'BaseService','AuthenticationSharedService',
//    function ($scope, ImageService, MerchantService, BaseService, AuthenticationSharedService) {
//        $scope.isAuthorized = AuthenticationSharedService.isAuthorized;
//
//        $scope.merchants = [];
//        $scope.merchant = {};
//        $scope.editMode = false;
//
//        $scope.loadAll = function(){
//            MerchantService.findAll().success(
//                function(data, status, headers, config){
//                    $scope.merchants = data;
//                }
//            );
//        };
//
//        $scope.create = function (merchant) {
//            MerchantService.create(merchant).success(
//                function(data, status, headers, config){
//                    $scope.merchant = BaseService.loadRefer(headers);
//                    $scope.merchants.push(merchant);
//                    $scope.merchant = {};
//                }
//            );
//        };
//
//        $scope.update = function (merchant) {
//            MerchantService.patch(merchant).success(
//                function(data, status, headers, config){
//                    $scope.merchant = BaseService.loadRefer(headers);
//                    $scope.merchant.remove(merchant);
//                    $scope.merchants.push($scope.merchant);
//                    $scope.merchant = {};
//                }
//            );
//        };
//
//        $scope.remove = function(id){
//            MerchantService.remove(id).success(
//                function(data, status, headers, config){
//                    $scope.loadAll();
//                }
//            );
//        };
//
//        $scope.loadAll();
//    }
//]);
//
//AngularSpringApp.controller('MerchantDetailController', ['$scope', '$routeParams', '$modal', 'MerchantService', 'BaseService', 'ShoppingCarService', 'AuthenticationSharedService',
//    function($scope, $routeParams, $modal, MerchantService, BaseService, ShoppingCarService, AuthenticationSharedService){
//        $scope.editMode = false;
//
//        $scope.addItem = function(product){
//            ShoppingCarService.addItem({number: 1, product: product });
//        };
//
//        $scope.isAuthorized = AuthenticationSharedService.isAuthorized;
//
//        $scope.newProduct = function(){
//            $scope.editMode = true;
//            $scope.product = {};
//        };
//
//        $scope.editProduct = function(product){
//            $scope.editMode = true;
//            $scope.product = product;
//        };
//
//        $scope.findMerchant = function(){
//            MerchantService.findById($routeParams.id).success(
//                function(data, status, headers, config){
//                    $scope.merchant = data;
//                }
//            );
//        };
//
//        $scope.findAllProduct = function(){
//            MerchantService.findAllProduct($routeParams.id).success(
//                function(data, status, headers, config){
//                    $scope.products = data;
//                }
//            );
//        };
//
//        $scope.createProduct = function(data){
//            alert(JSON.stringify(data));
//            MerchantService.createProduct($routeParams.id, data).success(
//                function(data, status, headers, config){
//                    var products = BaseService.loadRefer(headers);
//                    $scope.findAllProduct();
//                    $scope.product = {};
//                }
//            );
//        };
//
//        $scope.updateProduct = function(id, data){
//            MerchantService.patchProduct($routeParams.id, id, data).success(
//                function(data, status, headers, config){
//                    var products = BaseService.loadRefer(headers);
//                    $scope.findAllProduct();
//                    $scope.product = {};
//                }
//            );
//        };
//
//        $scope.removeProduct = function(id){
//            MerchantService.deleteProduct($routeParams.id, id).success(
//                function(data, status, headers, config){
//                    $scope.findAllProduct();
//                }
//            );
//        };
//
//        $scope.resetProduct = function(){
//            $scope.editMode = false;
//            $scope.product = {};
//        };
//
//        $scope.findAllComment = function(){
//            MerchantService.findAllComment($routeParams.id).success(
//                function(data, status, headers, config){
//                    $scope.comments = data;
//                }
//            );
//        };
//
//        $scope.findMerchant();
//        $scope.findAllProduct();
//        $scope.findAllComment();
//    }
//]);
//
//AngularSpringApp.controller('ShoppingCarController', ['$scope', 'ShoppingCarService',
//    function($scope, ShoppingCarService){
//        $scope.clear = function(product){
//            ShoppingCarService.addItem(product).success(
//                function(data, status, headers, config){
//                    $scope.shoppingCar = [];
//                }
//            );
//        };
//        $scope.find = function(){
//            ShoppingCarService.find().success(
//                function(data, status, headers, config){
//                    $scope.shoppingCar = data;
//                }
//            );
//        };
//        $scope.addItem = function(product){
//            ShoppingCarService.addItem({number: 1, product: product }).success(
//                function(data, status, headers, config){
//                    $scope.find();
//                }
//            );
//        };
//        $scope.minusItem = function(product){
//            ShoppingCarService.addItem({number: -1, product: product }).success(
//                function(data, status, headers, config){
//                    $scope.find();
//                }
//            );
//        };
//        $scope.removeItem = function(id){
//            ShoppingCarService.removeItem(id).success(
//                function(data, status, headers, config){
//                    $scope.find();
//                }
//            )
//        };
//        $scope.countShoppingCarItem = function(items){
//            if(items === null){
//                return 0;
//            }
//            var number = 0;
//            for(var i = 0; i < items.length; i++){
//                number += items[i].number;
//            }
//            return number;
//        };
//        $scope.countShoppingCarPrice = function(items){
//            if(items === null){
//                return 0;
//            }
//            var number = 0;
//            for(var i = 0; i < items.length; i++){
//                number += items[i].number * items[i].product.price;
//            }
//            return number;
//        };
//
//        $scope.find();
//
//    }
//]);
//
//AngularSpringApp.controller('AccountController', ['$scope',
//    function ($scope) {
//    }
//]);
//
//AngularSpringApp.controller('OrderController', ['$scope',
//    function ($scope) {
//        $scope.findInDeliveryOrders = function(){
//
//        };
//
//        $scope.findDeliveriedOrders = function(){
//
//        };
//
//        $scope.updateStatus = function(){
//
//        };
//
//        $scope.findMerchantOrders = function(){
//
//        };
//
//        $scope.findUserFinishedOrders = function(){
//
//        };
//
//        $scope.findUnfinishedOrders = function(){
//
//        };
//
//        $scope.createOrder = function(){
//
//        };
//
//        $scope.updateOrder = function(){
//
//        };
//
//        $scope.removeOrder = function(id){
//
//        };
//    }
//]);
//
//AngularSpringApp.controller('AdminController', ['$scope',
//    function ($scope) {
//    }
//]);