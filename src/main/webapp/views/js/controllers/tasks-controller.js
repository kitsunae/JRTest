/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').controller('TasksController',
    function ($http, $rootScope, $routeParams, $scope, TaskService, UserService, PagesService, $cookies) {

        var clicked = undefined;
        var indexOfLastTask = -1;
        var resultsOnPage = 5;
        $scope.currentPage=1;
        $scope.lastTask = null;
        $scope.tasks = [];
        $scope.pages = [];

        var init = function (user) {
            TaskService.getAll(UserService.getLinkToUserTasks(user))
                .then(function successCallback(data) {
                    $scope.tasks = data.data;
                }, function errorCallback(data) {
                    console.log(data.statusText);
                });
            PagesService.getTaskPages(resultsOnPage, user.number)
                .then(function successCallback(data) {
                    for (var i = 1; i <= data.data/5+1; ++i) {
                        $scope.pages.push(i);
                    }
                }, function errorCallback(data) {
                    console.log(data.statusText);
                });

            $scope.isClicked = function (task) {
                return task === clicked;
            };

            $scope.isAvailable = function () {
                return user.login == $cookies.get("userId");
            }
        };

        if ($rootScope.selectedUser == null) {
            UserService.getUser($routeParams.id)
                .then(function successCallback(response) {
                    $scope.user = response.data;
                    init($scope.user);
                }, function errorCallback(response) {
                    console.log('Server error' + response.statusText);
                });
        } else {
            $scope.user = $rootScope.selectedUser;
            $rootScope.selectedUser = null;
            init($scope.user);
        }

        $scope.setClicked = function (task) {
            for (var i = 0; i < $scope.tasks.length; ++i) {
                if ($scope.tasks[i] === task) {
                    indexOfLastTask = i;
                    $scope.lastTask = JSON.parse(JSON.stringify($scope.tasks[i]));
                }
            }
            clicked = task;
        };

        $scope.acceptChanges = function () {
            clicked = undefined;
            $scope.lastTask.done = $scope.lastTask.done || false;
            if ($scope.lastTask.createdAt == null) {
                TaskService.saveTask($scope.lastTask, $scope.user.number)
                    .then(function (response) {
                        $scope.tasks.push(response.data);
                    }, function (response) {
                        console.log(response.statusText);
                    });
            } else {
                $scope.lastTask = $scope.tasks[indexOfLastTask];
                TaskService.updateTask($scope.lastTask)
                    .then(function (response) {
                        $scope.tasks.splice(indexOfLastTask, 1, response.data);
                    }, function (response) {
                        console.log(response.statusText);
                    });
            }
            $scope.lastTask = null;
        };

        $scope.cancelChanges = function () {
            if (indexOfLastTask != -1) {
                $scope.tasks[indexOfLastTask] = $scope.lastTask;
            }
            $scope.lastTask = null;
            clicked = undefined;
        };

        $scope.remove = function (task) {
            for (var i = 0; i < $scope.tasks.length; ++i) {
                if ($scope.tasks[i] === task) {
                    $scope.lastTask = $scope.tasks[i];
                    $scope.tasks.splice(i, 1);
                }
            }
            TaskService.removeTask($scope.lastTask, $scope.lastTask.user.number);
        };

        $scope.getPage = function (page) {
            if (page>$scope.pages.length || page<1)
                return;
            TaskService.getAll(UserService.getLinkToUserTasks($scope.user), page-1, resultsOnPage)
                .then(function successCallback(data) {
                    $scope.tasks = data.data;
                }, function errorCallback(data) {
                    console.log(data.statusText);
                });
            $scope.currentPage = page;
        }
    });