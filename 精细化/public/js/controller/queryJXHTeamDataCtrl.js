/**
 * Created by Administrator on 2017/11/6 0006.
 */
app.controller('queryJXHDataCtrl', function ($scope, srQueryJXHApp) {
    $scope.yearlist = ["2016","2017","2018"];
    $scope.versionlist = ["1","2","3","4","5","6","7","8","9","10","11","12"];
    $scope.deptlist = ["所有部门","杭州开发一部","杭州开发二部","杭州开发三部","杭州开发四部","杭州开发五部","杭州研发支持部","杭州测试部"];
    $scope.monthchoose = "1";
    $scope.yearchoose = "2017";
    $scope.deptchoose = "所有部门";
})