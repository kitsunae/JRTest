/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').controller('UsersController',
    function ($rootScope, $window, $scope, UserService, PagesService, $cookies) {
    $scope.users = [];
    UserService.getAllUsers()
        .then(function successCallback(response) {
            $scope.users = response.data;
        }, function errorCallback(response) {
            console.log('Server error' + response.statusText);
        });

    $scope.pages = [];
    PagesService.getUserPages()
        .then(function successCallback(data) {
            for (var i = 1; i <= data.data; ++i) {
                result.push(i);
            }
        }, function errorCallback(data) {
            console.log('Server error' + data.statusText);
        });

    $scope.setUser = function (user) {
        $rootScope.selectedUser = user;
        $window.location.href = "#!/tasks/" + user.number;
    };

    $scope.isLoggedIn = function () {
        return $cookies.get("userId") != null;
    };

    $scope.logout = function () {
        $cookies.remove("userId");
        $cookies.remove("JSESSIONID");
    };


    $scope.login = function(user){
        UserService.login(user);
    };

    $scope.register = function () {
        UserService.addUser($scope.user)
            .then(function success(response){
                $scope.login(response.data);
                $window.location.href = "#!/tasks/" + response.data.number;
            }, function failure(error){
                if (error.statusCode != 200){
                    alert("Login not available!");
                }
                console.log(error.statusText);
            });
    };

    $scope.isAvailable = function(user){
        return user.login == $cookies.get("userId");
    };
});
