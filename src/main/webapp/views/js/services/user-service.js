/**
 * Created by lashi on 04.03.2017.
 */
angular.module('jrTest').factory('UserService', function UserService($http){
    var service = {};

    service.getUser = function(userId){
        return $http({method : "GET", url : "user/"+userId});
    };

    service.getLinkToUserTasks = function (user) {
        for (var i = 0; i<user.links.length; ++i){
            if (user.links[i].rel === "tasks"){
                return user.links[i].href;
            }
        }
    };

    service.login = function (data) {
        return $http({
            method: "POST",
            url: "login",
            params:{
                "username": data.login,
                "password": data.password
            },
            headers:{
                "Content-Type": "application/x-www-form-urlencoded"
            }
        })
            .then(function (response) {
                alert("log in successful");
            }, function (error) {
                alert("error logging in");
            });
    };

    service.getAllUsers = function(page, resultsOnPage){
        var pageNumber = page || 0;
        var results = resultsOnPage || 5;
        return $http({method : "GET", url : "user/all", params: {"page": pageNumber, "size": results}});
    };

    service.addUser = function (user) {
        return $http({
            method: "POST",
            url: "user",
            data: user
        });
    };

    return service;
});