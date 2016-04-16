(function() {
    'use strict';

    angular
        .module('myphotogramApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('photo', {
            parent: 'entity',
            url: '/photo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Photos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo/photos.html',
                    controller: 'PhotoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('photo-detail', {
            parent: 'entity',
            url: '/photo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Photo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo/photo-detail.html',
                    controller: 'PhotoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Photo', function($stateParams, Photo) {
                    return Photo.get({id : $stateParams.id});
                }]
            }
        })
        .state('photo.new', {
            parent: 'photo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-dialog.html',
                    controller: 'PhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                thumbnailUrl: null,
                                width: null,
                                height: null,
                                hidden: false,
                                creationDate: null,
                                year: null,
                                month: null,
                                day: null,
                                latitude: null,
                                longitude: null,
                                likes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('photo');
                });
            }]
        })
        .state('photo.edit', {
            parent: 'photo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-dialog.html',
                    controller: 'PhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Photo', function(Photo) {
                            return Photo.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('photo.delete', {
            parent: 'photo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-delete-dialog.html',
                    controller: 'PhotoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Photo', function(Photo) {
                            return Photo.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
