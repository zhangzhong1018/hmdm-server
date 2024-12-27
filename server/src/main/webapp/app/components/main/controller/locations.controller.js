// Localization completed
angular.module('headwind-kiosk')
    .controller('LocationsTabController', function ($scope, $rootScope, $state, $modal, alertService, confirmModal,
                                                    locationService, $window, localization) {
        $scope.search = {};

        $scope.paging = {
            currentPage: 1,
            pageSize: 50
        };

        $scope.$watch('paging.currentPage', function () {
            $window.scrollTo(0, 0);
        });

        $scope.init = function () {
            $rootScope.settingsTabActive = true;
            $rootScope.pluginsTabActive = false;
            $scope.paging.currentPage = 1;
            $scope.search();
        };

        $scope.search = function () {
            locationService.getAllLocations({value: $scope.search.searchValue},
                function (response) {
                    $scope.locations = response.data;
                });
        };

        $scope.editLocation = function (location) {
            var modalInstance = $modal.open({
                templateUrl: 'app/components/main/view/modal/location.html',
                controller: 'LocationModalController',
                resolve: {
                    location: function () {
                        return location;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.search();
            });
        };

        $scope.removeLocation = function (location) {
            let localizedText = localization.localize('question.delete.location').replace('${locationName}', location.name);
            confirmModal.getUserConfirmation(localizedText, function () {
                locationService.removeLocation({id: location.id}, function (response) {
                    if (response.status === 'OK') {
                        $scope.search();
                    } else {
                        alertService.showAlertMessage(localization.localize('error.notempty.location'));
                    }
                });
            });
        };

        $scope.init();
    })
    .controller('LocationModalController', function ($scope, $modalInstance, locationService, location, localization) {
        $scope.location = {};
        for (var prop in location) {
            if (location.hasOwnProperty(prop)) {
                $scope.location[prop] = location[prop];
            }
        }

        $scope.save = function () {
            $scope.errorMessage = '';

            if (!$scope.location.name) {
                $scope.errorMessage = localization.localize('error.empty.location.name');
            } else {
                var request = {};
                for (var prop in $scope.location) {
                    if ($scope.location.hasOwnProperty(prop)) {
                        request[prop] = $scope.location[prop];
                    }
                }

                locationService.updateLocation(request, function (response) {
                    if (response.status === 'OK') {
                        $modalInstance.close();
                    } else {
                        $scope.errorMessage = localization.localize('error.duplicate.location.name');
                    }
                });
            }
        };

        $scope.closeModal = function () {
            $modalInstance.dismiss();
        }
    });
