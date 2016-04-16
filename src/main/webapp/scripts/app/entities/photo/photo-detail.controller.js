'use strict';

angular.module('myphotogramApp')
    .controller('PhotoDetailController', function ($scope, $stateParams, Photo, User) {
        $scope.photo = {};
        $scope.load = function (id) {
            Photo.get({id: id}, function(result) {
              $scope.photo = result;
            });
        };
        $scope.load($stateParams.id);
    });
