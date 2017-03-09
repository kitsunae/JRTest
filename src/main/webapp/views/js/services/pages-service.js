angular.module('jrTest').factory('PagesService', function PagesServiceFactory($http) {
    var service = {};

    service.getTaskPages = function (tasksOnPage, userId) {
        return $http({
            method: "GET",
            url: "task/count",
            params: {
                "tasksOnPage": tasksOnPage,
                "userId": userId
            }
        });
    };

    service.getUserPages = function () {
        return $http({method: "GET", url: "user/count"});
    };

    return service;
});