'use strict';

angular.module('myphotogramApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
