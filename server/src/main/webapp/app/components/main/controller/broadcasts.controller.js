// Localization completed
angular.module('headwind-kiosk')
    .controller('BroadcastTabController', function ($scope, $rootScope, $state, $modal, alertService, confirmModal,
                                                    broadcastService, $window, localization) {
        $scope.search = {};

        $scope.paging = {
            currentPage: 1,
            pageSize: 50
        };

        $scope.localization = localization;
        $scope.datetimeFormat = localization.localize('broadcasts.datetime.format');

        $scope.$watch('paging.currentPage', function () {
            $window.scrollTo(0, 0);
        });

        $scope.init = function () {
            $rootScope.settingsTabActive = false;
            $rootScope.pluginsTabActive = false;
            $scope.paging.currentPage = 1;
            $scope.search();
        };

        $scope.search = function () {
            broadcastService.getAllBroadcasts({value: $scope.search.searchValue},
                function (response) {
                    $scope.broadcasts = response.data;
                });
        };

        $scope.editBroadcast = function (broadcast) {
            var modalInstance = $modal.open({
                templateUrl: 'app/components/main/view/modal/broadcast.html',
                controller: 'BroadcastModalController',
                resolve: {
                    broadcast: function () {
                        var date = new Date(broadcast.startTime);
                        var hours = date.getHours().toString().padStart(2, '0');
                        var minutes = date.getMinutes().toString().padStart(2, '0');
                        broadcast.time = date
                        console.log("==================")
                        console.log(broadcast.startTime)
                        console.log(date)
                        console.log(hours)
                        console.log(minutes)
                        console.log(broadcast.time)
                        console.log(JSON.stringify(broadcast))
                        return broadcast;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.search();
            });
        };

        $scope.removeBroadcast = function (broadcast) {
            let localizedText = localization.localize('question.delete.location').replace('${locationName}', broadcast.number);
            confirmModal.getUserConfirmation(localizedText, function () {
                broadcastService.removeBroadcast({id: broadcast.id}, function (response) {
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
    .controller('BroadcastModalController', function ($scope, $modalInstance, broadcastService, deviceService, broadcast, localization) {
        $scope.broadcast = {};
        $scope.devicesList = [];

        for (var prop in broadcast) {
            if (broadcast.hasOwnProperty(prop)) {
                $scope.broadcast[prop] = broadcast[prop];
            }
        }
        var request = {
            groupId: -1,
            configurationId: -1,
            locationId: -1,
            pageNum: 1,
            pageSize: 1000,
            sortBy: null,
            sortDir: "ASC"
        };
        deviceService.getAllDevices(request,function (response) {
            $scope.devicesList = response.data.devices.items.map(function (device) {
                return {id: device.id, label: device.number};
            });
        });

        $scope.devicesSelection = (broadcast.devices || []).map(function (device) {
            return {id: device.id};
        });

        $scope.localization = localization;
        $scope.dateFormat = localization.localize('broadcasts.date.format');
        $scope.datePickerOptions = { 'show-weeks': false };
        $scope.openDatePickers = {
            'dateFrom': false
        };

        $scope.openDateCalendar = function( $event, isStartDate ) {
            $event.preventDefault();
            $event.stopPropagation();
            if ( isStartDate ) {
                $scope.openDatePickers.dateFrom = true;
            }
        };

        $scope.tableFilteringTexts = {
            'buttonDefaultText': localization.localize('table.filtering.no.selected.device'),
            'checkAll': localization.localize('table.filtering.check.all'),
            'uncheckAll': localization.localize('table.filtering.uncheck.all'),
            'dynamicButtonTextSuffix': localization.localize('table.filtering.suffix.device')
        };

        $scope.save = function () {
            $scope.errorMessage = '';

            if (!$scope.broadcast.number) {
                $scope.errorMessage = localization.localize('error.empty.broadcast.name');
            } else {
                var request = {};
                $scope.broadcast.devices = $scope.devicesSelection;
                for (var prop in $scope.broadcast) {
                    if ($scope.broadcast.hasOwnProperty(prop)) {
                        request[prop] = $scope.broadcast[prop];
                        if(prop === "startTime"){
                            const date = new Date(request[prop]);
                            const time = $scope.broadcast.time;
                            const timeDate = new Date(time);
                            date.setHours(timeDate.getHours())
                            date.setMinutes(timeDate.getMinutes())
                            request[prop] = Math.floor(date.getTime());
                        }
                    }
                }
                broadcastService.updateBroadcast(request, function (response) {
                    if (response.status === 'OK') {
                        $modalInstance.close();
                    } else {
                        $scope.errorMessage = localization.localize('error.duplicate.broadcast.name');
                    }
                });
            }
        };

        $scope.closeModal = function () {
            $modalInstance.dismiss();
        }
    });
