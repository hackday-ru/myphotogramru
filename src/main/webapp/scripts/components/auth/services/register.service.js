'use strict';

angular.module('myphotogramApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


