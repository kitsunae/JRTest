/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').config(function ($routeProvider) {
    $routeProvider.when("/users", {
        templateUrl: "templates/pages/users/index.html",
        controller: 'UsersController'
    })
        .when("/", {
            templateUrl: "templates/pages/users/index.html",
            controller: 'UsersController'
        })
        .when("/tasks/:id", {
            templateUrl: "templates/pages/tasks/index.html",
            controller: 'TasksController'
        })
        .otherwise({redirectTo: "/"});
});