angular.module('jrTest').controller('UsersController',
    function ($rootScope, $window, $scope, UserService, PagesService, $cookies) {
        $scope.users = [];
        $scope.currentPage = 1;
        UserService.getAllUsers()
            .then(function successCallback(response) {
                $scope.users = response.data;
            }, function errorCallback(response) {
                console.log('Server error' + response.statusText);
                $window.location.href = "#!/error";
            });

        $scope.pages = [];
        PagesService.getUserPages()
            .then(function successCallback(data) {
                for (var i = 1; i <= data.data/5+1; ++i) {
                    $scope.pages.push(i);
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
            UserService.logout();
            $cookies.remove("userId");
            $cookies.remove("JSESSIONID");
            $scope.credentials = null;
        };


        $scope.login = function (user) {
            UserService.login(user);
        };

        $scope.register = function () {
            UserService.addUser($scope.user)
                .then(function success(response) {
                    $scope.login($scope.user);
                    $window.location.href = "#!/tasks/" + response.data.number;
                }, function failure(error) {
                    if (error.statusCode != 200) {
                        alert("Login not available!");
                    }
                    console.log(error.statusText);
                });
        };

        $scope.getPage = function(page){
            if (page>$scope.pages.length || page<1)
                return;
            UserService.getAllUsers(page-1, 5)
                .then(function successCallback(response) {
                    $scope.users = response.data;
                }, function errorCallback(response) {
                    console.log('Server error' + response.statusText);
                });
            $scope.currentPage = page;
        };

        $scope.isAvailable = function (user) {
            return user.login == $cookies.get("userId");
        };

        $scope.getUser = function (user) {
            $rootScope.selectedUser = user;
            $window.location.href = "#!/users/" + user.number;
        };
    });
