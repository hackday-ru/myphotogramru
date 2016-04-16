(function() {
    'use strict';
    angular
        .module('myphotogramApp')
        .factory('Photo', Photo);

    Photo.$inject = ['$resource', 'DateUtils'];

    function Photo ($resource, DateUtils) {
        var resourceUrl =  'api/photos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertLocalDateFromServer(data.creationDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocalDateToServer(data.creationDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocalDateToServer(data.creationDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
