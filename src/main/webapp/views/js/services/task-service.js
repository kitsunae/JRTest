/**
 * Created by lashi on 04.03.2017.
 */
angular.module('jrTest').factory('TaskService', function TaskServiceFactory($http) {
    var service = {};

    service.getAll = function (link) {
        return $http({method: "GET", url: link});
    };

    service.getTaskSelfUrl = function (task) {
        for (var i = 0; i<task.links.length; ++i) {
            if (task.links[i].rel === "self"){
                return task.links[i].href;
            }
        }
    };

    service.saveTask = function (task, userId) {
        return $http({method: "POST", url: "task", params: {'userId': userId}, data: task});
    };

    service.updateTask = function (task) {
        return $http({method: "PUT", url: this.getTaskSelfUrl(task), params:{}, data: task});
    };

    service.removeTask = function (task, userId) {
        return $http({method: "DELETE", url: this.getTaskSelfUrl(task), params: {'userId': userId}});
    };

    return service;
});