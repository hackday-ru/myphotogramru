(function() {
    'use strict';

    angular
        .module('myphotogramApp')
        .controller('PhotoDialogController', PhotoDialogController);

    PhotoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Photo', 'User'];

    function PhotoDialogController ($scope, $stateParams, $uibModalInstance, entity, Photo, User) {
        var vm = this;
        vm.photo = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Photo.get({id : id}, function(result) {
                vm.photo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myphotogramApp:photoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.photo.id !== null) {
                Photo.update(vm.photo, onSaveSuccess, onSaveError);
            } else {
                Photo.save(vm.photo, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
