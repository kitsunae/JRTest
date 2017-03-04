/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').controller('UsersController', function ($http, $rootScope, $window) {
    var self = this;
    self.users = [];
    $http({
        method : "GET",
        url : "user/all"
    }).then(function successCallback(response) {
        self.users = response.data;
    }, function errorCallback(response) {
        console.log('Server error'+response.statusText);
    });
    $http(
        {
            method: "GET",
            url : "user/all/count"
        }
        ).then(function successCallback(data) {
        self.pages = [];
        for (var i = 1; i <= data.data; ++i) {
            self.pages.push(i);
        }
    }, function errorCallback (data) {
        console.log('Server error'+data.statusText);
    });
    this.setUser = function (user) {
        $rootScope.user = user;
        $window.location.href="#!/tasks/"+user.number;
    }
});
