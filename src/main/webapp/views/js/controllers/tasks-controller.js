/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').controller('TasksController',function ($http, $rootScope, $routeParams) {
    var clicked = 0;
    var self = this;
    self.lastTask = null;
    $http({method: "GET", url: $rootScope.user.links[1].href})
        .then(function successCallback(data) {
            self.tasks = data.data;
        }, function errorCallback(data) {
            console.log(data.statusText);
        });
    $http({method: "GET", url: "task/all/count", params: {"tasksOnPage": 20, "userId": $rootScope.user.number}})
        .then(function successCallback(data) {
            self.pages = [];
            for (var i = 1; i <= data; ++i) {
                self.pages.push(i);
            }
        }, function errorCallback(data) {
            console.log(data.statusText);
        });
    var indexOfLastTask = -1;

    self.isClicked = function (task) {
        return task === clicked;
    };

    self.setClicked = function (task) {
        for (var i = 0; i < self.tasks.length; ++i) {
            if (self.tasks[i].createdAt === task) {
                indexOfLastTask = i;
                self.lastTask = JSON.parse(JSON.stringify(self.tasks[i]));
            }
        }
        clicked = task;
    };

    self.acceptChanges = function () {
        clicked = 0;
        self.lastTask.done = self.lastTask.done || false;
        if (self.lastTask.createdAt == null) {
            $http({method: "POST", url: "task", params: {'userId': $rootScope.user.number}, data: self.lastTask})
                .then(function (response) {
                    self.tasks.push(response.data);
                }, function (response) {
                    console.log(response.statusText);
                });
        } else {
            self.lastTask = self.tasks[indexOfLastTask];
            $http({method: "PUT", url: self.lastTask.links[0].href, params:{}, data: self.lastTask})
                .then(function (response) {
                    self.tasks.splice(i,indexOfLastTask);
                    self.tasks.push(response.data);
                }, function(response){
                    console.log(response.statusText);
                });
        }
        self.lastTask = null;
    };

    self.cancelChanges = function () {
        if (indexOfLastTask != -1) {
            self.tasks[indexOfLastTask] = self.lastTask;
        }
        self.lastTask = null;
        clicked = 0;
    };

    self.remove = function (task) {
        for (var i = 0; i < self.tasks.length; ++i) {
            if (self.tasks[i].createdAt === task) {
                self.lastTask = self.tasks[i];
                self.tasks.splice(i,1);
            }
        }
        $http({method: "DELETE", url: self.lastTask.links[0].href, params: {'userId': $rootScope.user.number}});
    }
});