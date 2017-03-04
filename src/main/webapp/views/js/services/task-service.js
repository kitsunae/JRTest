/**
 * Created by lashi on 04.03.2017.
 */
angular.module('jrTest').factory('TaskService', function TaskServiceFactory($http) {
    var service = {};
    service.getAll = function (link) {
        var result = [];
        $http({method: "GET", url: link}).then(function successCallback(data) {
            for (var i = 1; i <= data; ++i) {
                result.push(i);
            }
        }, function errorCallback(data) {
            console.log(data.statusText);
        });
        return result;
    };

    service.getTaskSelfUrl = function (task) {
        for (var link in task.links) {
            if (link.rel === "self"){
                return link.href;
            }
        }
    };

    service.saveTask = function (task) {

    };

    return service;
});