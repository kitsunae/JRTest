angular.module('jrTest').controller('UserInformationController', function($rootScope, $scope, UserService, $cookies, $routeParams, $window){
    $scope.edition = false;
    if ($rootScope.selectedUser==null){
       UserService.getUser($routeParams.id)
           .then(function (response) {
               $scope.user = response.data;
           }, function (error) {
               console.log(error.status);
           });
    }else{
        $scope.user = $rootScope.selectedUser;
        $rootScope.selectedUser = null;
    }

    $scope.getUserTasks = function () {
        $rootScope.selectedUser = $scope.user;
        $window.location.href = "#!/tasks/" + $scope.user.number;
    };

    $scope.startEditing = function () {
        $scope.edition = true;
    };

    $scope.edit = function () {
        UserService.editUser($scope.user)
            .then(function (response) {
                UserService.logout();
                var credentials = {login: $scope.user.login, password: $scope.user.password};
                UserService.login(credentials);
                $scope.user = response.data;
            }, function(error){
                console.log(error.status);
            });
    };

    $scope.deleteUser = function(){
        UserService.deleteUser($scope.user)
            .then(function(response){
                $window.location.href="#!";
            }, function (error) {
                console.log(error.status);
            });
    };

    $scope.isAvailable = function () {
        return $scope.user.login == $cookies.get("userId");
    };
});