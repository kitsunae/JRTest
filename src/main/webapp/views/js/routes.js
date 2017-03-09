angular.module('jrTest').config(function ($routeProvider) {
    $routeProvider.when("/users", {
            templateUrl: "templates/pages/users/index.html",
            controller: 'UsersController'
        })
        .when("/users/:id",{
            templateUrl: "templates/pages/users/user.html",
            controller: 'UserInformationController'
        })
        .when("/", {
            templateUrl: "templates/pages/users/index.html",
            controller: 'UsersController'
        })
        .when("/tasks/:id", {
            templateUrl: "templates/pages/tasks/index.html",
            controller: 'TasksController'
        })
        .when("/register", {
            templateUrl: "templates/pages/registration/index.html",
            controller: 'UsersController'
        })
        .when("/error", {
            templateUrl: "error-page.html"
        })
        .otherwise({redirectTo: "/"});
});