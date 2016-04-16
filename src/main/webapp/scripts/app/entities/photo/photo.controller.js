'use strict';

angular.module('myphotogramApp')
    .controller('PhotoController', function ($scope, Photo, User, ParseLinks) {
        $scope.photos = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Photo.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.photos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Photo.get({id: id}, function(result) {
                $scope.photo = result;
                $('#savePhotoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.photo.id != null) {
                Photo.update($scope.photo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Photo.save($scope.photo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Photo.get({id: id}, function(result) {
                $scope.photo = result;
                $('#deletePhotoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Photo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePhotoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photo = {url: null, thumbnailUrl: null, width: null, height: null, likes: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
