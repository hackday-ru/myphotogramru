'use strict';

angular.module('myphotogramApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('account', {
                abstract: true,
                parent: 'site'
            });
    });
