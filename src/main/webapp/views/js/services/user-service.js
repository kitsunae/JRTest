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

    service.getAllUsers = function(){
        return $http({method : "GET", url : "user/all"});
    };

    return service;
});