angular.module('jrTest').controller('TasksController',
    function ($http, $rootScope, $routeParams, $scope, TaskService, UserService, PagesService, $cookies, $window) {
        var clicked = undefined;
        var indexOfLastTask = -1;
        var resultsOnPage = 5;
        $scope.currentPage = 1;
        $scope.lastTask = null;
        $scope.tasks = [];
        $scope.pages = [];
        $scope.mode = "all";

        var init = function (user) {
            TaskService.getAll(UserService.getLinkToUserTasks(user))
                .then(function successCallback(data) {
                    $scope.tasks = data.data;
                }, function errorCallback(data) {
                    console.log(data.statusText);
                    $window.location.href = "#!/error";
                });
            PagesService.getTaskPages(resultsOnPage, user.number)
                .then(function successCallback(data) {
                    for (var i = 1; i <= data.data / 5 + 1; ++i) {
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

        $scope.changeTaskStatus = function (task) {
            $scope.setClicked(task);
            task.done = !task.done;
            $scope.acceptChanges();
        };

        $scope.acceptChanges = function () {
            clicked = undefined;
            $scope.lastTask.done = $scope.lastTask.done || false;
            if ($scope.lastTask.createdAt == null) {
                TaskService.saveTask($scope.lastTask, $scope.user.number)
                    .then(function (response) {
                        $scope.tasks.push(response.data);
                        $scope.lastTask = null;
                        indexOfLastTask = -1;
                    }, function (response) {
                        console.log(response.statusText);
                    });
            } else {
                TaskService.updateTask($scope.tasks[indexOfLastTask])
                    .then(function (response) {
                        $scope.tasks.splice(indexOfLastTask, 1, response.data);
                        $scope.lastTask = null;
                        indexOfLastTask = -1;
                    }, function (response) {
                        console.log(response.statusText);
                        $scope.tasks[indexOfLastTask] = $scope.lastTask;
                        indexOfLastTask = -1;
                    });
            }
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

        $scope.getPage = function (mode, page) {
            page = page || 1;
            if (page > $scope.pages.length || page < 1)
                return;
            var link = undefined;
            switch (mode) {
                case 'all': {
                    link = UserService.getLinkToUserTasks($scope.user);
                    break;
                }
                case 'done': {
                    link = UserService.getLinkToDoneTasks($scope.user);
                    break;
                }
                case 'undone': {
                    link = UserService.getLinkToUndoneTasks($scope.user);
                    break;
                }
            }
            TaskService.getAll(link, page - 1, resultsOnPage)
                .then(function successCallback(data) {
                    $scope.tasks = data.data;
                }, function errorCallback(data) {
                    console.log(data.statusText);
                });
            $scope.mode = mode;
            $scope.currentPage = page;
        };
    });