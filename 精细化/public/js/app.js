/**
 * Created by Administrator on 2017/10/27 0027.
 */
'use strict';
var app = angular.module('platform',['ui.router']);
app.run(['$rootScope', '$state', '$stateParams',
    function($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    }
]);
/*app.run(['$rootScope','$state','$stateParams',
    function ($rootScope,$state,$stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
}]);*/
app.config(['$controllerProvider','$compileProvider','$filterProvider','$provide',
    function ($controllerProvider, $compileProvider, $filterProvider,$provide) {
        app.controller = $controllerProvider.register;
        app.directive = $compileProvider.directive;
        app.filter = $filterProvider.register;
        app.factory = $provide.factory;
        app.service = $provide.service;
        app.constant = $provide.constant;
        app.value = $provide.value
    }]);
app.config(['$stateProvider','$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/logon");//logon
    //$urlRouterProvider.otherwise("/manager/application");
    $stateProvider.state("logon",{
        url:"/logon",
        templateUrl:"views/logon.html"
    }).state("manager",{
        url:"/manager",
        templateUrl:"views/manager.html"
    }).state("manager.department",{
        url:"/department",
        templateUrl:"views/ref/dept.html"
    }).state("manager.application",{
        url:"/application",
        templateUrl:"views/ref/app.html"
    }).state("manager.project",{
            url:"/project",
            templateUrl:"views/ref/proj.html"
    }).state("manager.team",{
            url:"/team",
            templateUrl:"views/ref/test.html"
        })
    //质量指标
        .state("manager.quality_department",{
            url:"/department",
            templateUrl:"views/quality/dept.html"
        }).state("manager.quality_application",{
            url:"/application",
            templateUrl:"views/quality/app.html"
        }).state("manager.quality_project",{
            url:"/project",
            templateUrl:"views/quality/proj.html"
        }).state("manager.quality_team",{
            url:"/team",
            templateUrl:"views/quality/team.html"
        })
    ;
}])