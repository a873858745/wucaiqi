/**
 * Created by Administrator on 2017/10/27 0027.
 */
app.controller('logonCtrl', function ($scope, $http, $location) {
    $scope.logonAuth = function () {
        console.log("logon--------");
        var username = document.getElementById("form-username").value;
        var password = document.getElementById("form-password").value;
        $http({
            url:'/logon',
            method:'POST',
            data:{username:username,password:password}
        }).then(function successCallback(response) {
            // 请求成功执行代码
            var data = response.data;
            console.log("response.data----------"+data);
            console.log("http----/logon");
            if(data=="0"){
                $location.path("/manager/application");
            }else{
                alert("用户名或者密码错误！");
            }
        }, function errorCallback(response) {
            // 请求失败执行代码
            console.log(response.status);
            console.log("post---response---error");
        });
    }
});