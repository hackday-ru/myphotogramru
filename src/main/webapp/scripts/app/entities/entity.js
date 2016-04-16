'use strict';

angular.module('myphotogramApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            });
    });
