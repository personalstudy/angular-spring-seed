'use strict';

/* Filters */

AngularSpringApp.filter('interpolate', ['version', function (version) {
    return function (text) {
        return String(text).replace(/\%VERSION\%/mg, version);
    }
}]).filter('orderStatus', [function(){
    return function(text){
        return text
            .replace('ORDERED', '已下单')
            .replace('NOTIFIED', '商家调度')
            .replace('DELIVERED', '调度中')
            .replace('ON_THE_WAY', '配送中')
            .replace('FINISHED', '已完成');
    }
}]);
