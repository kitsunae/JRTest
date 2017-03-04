/**
 * Created by lashi on 01.03.2017.
 */
angular.module('jrTest').controller('UsersController', function ($http, $rootScope, $window, $scope, UserService, PagesService) {
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
    }
});
