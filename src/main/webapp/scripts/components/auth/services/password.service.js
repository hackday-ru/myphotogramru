'use strict';

angular.module('myphotogramApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {
        });
    });

angular.module('myphotogramApp')
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {
        })
    });

angular.module('myphotogramApp')
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {
        })
    });
