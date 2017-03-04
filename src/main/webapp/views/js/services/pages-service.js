/**
 * Created by lashi on 04.03.2017.
 */
angular.module('jrTest').factory('PagesService', function PagesServiceFactory($http){
    var service = {};

    service.getTaskPages = function(tasksOnPage, userId){
        var result = [];
        $http({method: "GET", url: "task/all/count", params: {"tasksOnPage": tasksOnPage, "userId": userId}})
            .then(function successCallback(data) {
                for (var i = 1; i <= data.data; ++i) {
                    result.push(i);
                }
            }, function errorCallback(data) {
                console.log(data.statusText);
            });
        return result;
    };

    service.getUserPages = function(){
        var result = [];
        $http(
            {
                method: "GET",
                url : "user/all/count"
            }
        ).then(function successCallback(data) {
            for (var i = 1; i <= data.data; ++i) {
                result.push(i);
            }
        }, function errorCallback (data) {
            console.log('Server error'+data.statusText);
        });
        return result;
    };

    return service;
});