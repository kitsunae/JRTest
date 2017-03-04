/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').config(function ($routeProvider) {
    $routeProvider.when("/users", {
        templateUrl: "templates/pages/users/index.html",
        controller: 'UsersController',
        controllerAs: 'usersCtrl'
    })
        .when("/", {
            templateUrl: "templates/pages/users/index.html",
            controller: 'UsersController',
            controllerAs: 'usersCtrl'
        })
        .when("/tasks/:id", {
            templateUrl: "templates/pages/tasks/index.html",
            controller: 'TasksController',
            controllerAs: 'tasksCtrl'
        })
        .otherwise({redirectTo: "/"});
});