'use strict';

angular.module('myphotogramApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photo', {
                parent: 'entity',
                url: '/photo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'myphotogramApp.photo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photo/photos.html',
                        controller: 'PhotoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('photo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('photoDetail', {
                parent: 'entity',
                url: '/photo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'myphotogramApp.photo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photo/photo-detail.html',
                        controller: 'PhotoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('photo');
                        return $translate.refresh();
                    }]
                }
            });
    });
