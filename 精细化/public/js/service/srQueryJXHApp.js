/**
 * Created by Administrator on 2017/11/6 0006.
 */
app.factory('srQueryJXHApp',function ($http) {
    var factory = {
        queryAll:function(){
            var queryResultProcess = this.queryResultProcess;
            return $http({
                url:"/dbservice/queryJXHAppAll",
                method:"post",
                data:{}
            }).then(function(res){
                var resultData = res.data;
                queryResultProcess(resultData);
                return resultData;
            }, function () {
                console.log("error");
                return false;
            });
        },
        queryResultProcess:function(resultData){
            for(var i=0;i<resultData.jxhAppArray.length;i++){
                
            }
        }
    };
    console.log("srQueryJXHApp loaded");
    return factory;
})