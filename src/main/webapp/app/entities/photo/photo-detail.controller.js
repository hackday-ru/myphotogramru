(function() {
    'use strict';

    angular
        .module('myphotogramApp')
        .controller('PhotoDetailController', PhotoDetailController);

    PhotoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Photo', 'User'];

    function PhotoDetailController($scope, $rootScope, $stateParams, entity, Photo, User) {
        var vm = this;
        vm.photo = entity;
        vm.load = function (id) {
            Photo.get({id: id}, function(result) {
                vm.photo = result;
            });
        };
        var unsubscribe = $rootScope.$on('myphotogramApp:photoUpdate', function(event, result) {
            vm.photo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
