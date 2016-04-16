(function() {
    'use strict';

    angular
        .module('myphotogramApp')
        .controller('PhotoDeleteController',PhotoDeleteController);

    PhotoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Photo'];

    function PhotoDeleteController($uibModalInstance, entity, Photo) {
        var vm = this;
        vm.photo = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Photo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
