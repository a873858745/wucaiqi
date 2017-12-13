/**
 * Created by Administrator on 2017/11/6 0006.
 */
app.controller('queryJXHAppDataCtrl', function ($scope,$http) {
    $scope.yearlist = ["2016","2017","2018"];
    $scope.versionlist = ["1","2","3","4","5","6","7","8","9","10","11","12"];
    $scope.deptlist = ["所有部门","杭州开发一部","杭州开发二部","杭州开发三部","杭州开发四部","杭州开发五部","杭州研发支持部","杭州测试部"];
    $scope.monthchoose = "1";
    $scope.yearchoose = "2017";
    $scope.deptchoose = "所有部门";
    var select = document.getElementById("jxhAppSelect");
    queryAll().then(function (result) {
        $scope.jxhappdata = result.jxhAppArray;
        var lastupdate = result.lastupdate;
        console.log("queryAll--lastupdate" + lastupdate);
        var temp = parseInt(lastupdate.substring(0,4))+"年"+parseInt(lastupdate.substring(4,6))+"月"+
            parseInt(lastupdate.substring(6,8))+"日";
        console.log("queryAll--lastupdatetemp" + temp);
        $scope.lastupdate = temp;
            //+"month"+lastupdate.substring(6,8)+"day"+lastupdate.substring(8,10)+"hour"+lastupdate.substring(10,12)+"minute");
        $scope.yearchoose = result.year;
        console.log("$scope.yearchoose"+$scope.yearchoose);
        $scope.monthchoose = result.month;
        var index = result.month;
        console.log("index--"+index);
        var form = document.getElementById("chooseversion_form_jxh_app");

        //初始化复选框
        var tagElements = form.getElementsByTagName("input");
        tagElements[index-1].checked = true;
        //向select中添加option
        select.options.length = 0;//将select的内容置空
        $scope.jxhAppArray = result.jxhAppArray;//获取应用的名字
        var appArray = $scope.jxhAppArray;
        for(var i=0;i<appArray.length;i++){
            select.options.add(new Option(appArray[i]["appShortName"],appArray[i]["appShortName"]));
        }
        var myChart = echarts.init(document.getElementById("jxhAppEChartMap"));
        myChart.setOption(getChartsOptions(result.jxhAppArray,result.year,result.month));
        window.onresize = myChart.resize;

    });
    $scope.queryJXHAppDataByApp = function () {
        var jxhAppSelect = document.getElementById("jxhAppSelect");
        var option  = jxhAppSelect.value;
        console.log("option-------"+option);
        var form = document.getElementById("chooseversion_form_jxh_app");
        var elements = new Array();
        var tagElements = form.getElementsByTagName("input");
        for(i=0;i<tagElements.length;i++){
            elements.push(tagElements[i]);
        }
        var choosedVersionArr = new Array();
        for(i=0;i<elements.length;i++){
            if(elements[i].checked){
                choosedVersionArr.push(elements[i].value);
            }
        }
        queryJXHAppDataByApp($scope.yearchoose,$scope.deptchoose,choosedVersionArr,option).then(function (result) {
            $scope.jxhappdata = result.jxhAppArray;
            $scope.monthchoose = result.month.toString();
            var myChart = echarts.init(document.getElementById("jxhAppEChartMap"));
            myChart.setOption(getChartsOptions(result.jxhAppArray,result.year,choosedVersionArr));
            window.onresize = myChart.resize;
        });
    };
    $scope.queryAppDataByVersion = function () {
        var form = document.getElementById("chooseversion_form_jxh_app");
        var elements = new Array();
        var tagElements = form.getElementsByTagName("input");
        for(i=0;i<tagElements.length;i++){
            elements.push(tagElements[i]);
        }
        var choosedVersionArr = new Array();
        for(i=0;i<elements.length;i++){
            if(elements[i].checked){
                choosedVersionArr.push(elements[i].value);
            }
        }
        queryAppDataByVersion($scope.yearchoose,$scope.deptchoose,choosedVersionArr).then(function (result) {
            $scope.jxhappdata = result.jxhAppArray;
            $scope.monthchoose = result.month.toString();
            select.options.length = 0;//将select的内容置空
            $scope.jxhAppArray = result.jxhAppArray;//获取应用的名字
            var appArray = $scope.jxhAppArray;
            for(var i=0;i<appArray.length;i++){
                select.options.add(new Option(appArray[i]["appShortName"],appArray[i]["appShortName"]));
            }
            var myChart = echarts.init(document.getElementById("jxhAppEChartMap"));
            myChart.setOption(getChartsOptions(result.jxhAppArray,result.year,choosedVersionArr));
            window.onresize = myChart.resize;
        })
    }

    function queryJXHAppDataByApp(year,dept,choosedVersionArr,option){
        return $http({
            url:"/dbservice/queryJXHAppDataByApp",
            method:"post",
            data:{year:year,dept:dept,choosedVersionArr:choosedVersionArr,option:option}
        }).then(function(res){
            var resultData = res.data;
            console.log("收到数据啦---" + resultData.jxhAppArray.length)
            queryResultProcess(resultData);
            return resultData;
        }, function () {
            console.log("error");
            return false;
        });
    }
    function queryAll(){
        return $http({
            url:"/dbservice/queryJXHAppAll",
            method:"post",
            data:{}
        }).then(function(res){
            var resultData = res.data;
            console.log("收到数据啦---" + resultData.jxhAppArray.length)
            queryResultProcess(resultData);
            return resultData;
        }, function () {
            console.log("error");
            return false;
        });
    }
    function queryAppDataByVersion(year,dept,choosedVersionArr){
        return $http({
            url:'/dbservice/queryAppDataByVersion',
            method:'post',
            data:{
                year:year,dept:dept,choosedVersionArr:choosedVersionArr
            }
        }).then(function(res){
            var resultData = res.data;
            queryResultProcess(resultData);
            return resultData;
        },function(){
            console.error("error");
            return false;
        })
    }
    function queryResultProcess(resultData){
        for(var i=0;i<resultData.jxhAppArray.length;i++){
            if(resultData.jxhAppArray[i]['dept']=="杭州测试部"){
                resultData.jxhAppArray[i]['RCompCoding']='-';
            }else if(resultData.jxhAppArray[i]['RCompCoding']!='' && resultData.jxhAppArray[i]['RCompCoding']!='-'){
                resultData.jxhAppArray[i]['RCompCoding'] = parseFloat(resultData.jxhAppArray[i]['RCompCoding']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['dept']=="杭州测试部"){
                resultData.jxhAppArray[i]['deptRCompCoding']='-';
            }else if(resultData.jxhAppArray[i]['deptRCompCoding']!='' && resultData.jxhAppArray[i]['deptRCompCoding']!='-'){
                resultData.jxhAppArray[i]['deptRCompCoding'] = parseFloat(resultData.jxhAppArray[i]['deptRCompCoding']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['ADLMWlColl']!=''){
                resultData.jxhAppArray[i]['ADLMWlColl'] = parseFloat(resultData.jxhAppArray[i]['ADLMWlColl']).toFixed(2);
            }
            if(resultData.jxhAppArray[i]['ImpoWl']!=''){
                resultData.jxhAppArray[i]['ImpoWl'] = parseFloat(resultData.jxhAppArray[i]['ImpoWl']).toFixed(2);
            }
            if(resultData.jxhAppArray[i]['RImpoWl']!=''){
                resultData.jxhAppArray[i]['RImpoWl'] = parseFloat(resultData.jxhAppArray[i]['RImpoWl']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['RCompDemaAnalWl']!=''){
                resultData.jxhAppArray[i]['RCompDemaAnalWl'] = parseFloat(resultData.jxhAppArray[i]['RCompDemaAnalWl']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['deptRCompDemaAnalWl']!=''){
                resultData.jxhAppArray[i]['deptRCompDemaAnalWl'] = parseFloat(resultData.jxhAppArray[i]['deptRCompDemaAnalWl']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['RCompDetaDesign']!=''){
                resultData.jxhAppArray[i]['RCompDetaDesign'] = parseFloat(resultData.jxhAppArray[i]['RCompDetaDesign']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['deptRCompDetaDesign']!=''){
                resultData.jxhAppArray[i]['deptRCompDetaDesign'] = parseFloat(resultData.jxhAppArray[i]['deptRCompDetaDesign']*100).toFixed(0)+'%';
            }
            /*if(resultData.jxhAppArray[i]['RCompCoding']!=''){
                resultData.jxhAppArray[i]['RCompCoding'] = parseFloat(resultData.jxhAppArray[i]['RCompCoding']*100).toFixed(0)+'%';
            }*/
            /*if(resultData.jxhAppArray[i]['deptRCompCoding']!=''){
                resultData.jxhAppArray[i]['deptRCompCoding'] = parseFloat(resultData.jxhAppArray[i]['deptRCompCoding']*100).toFixed(0)+'%';
            }*/
            if(resultData.jxhAppArray[i]['RCompTest']!=''){
                resultData.jxhAppArray[i]['RCompTest'] = parseFloat(resultData.jxhAppArray[i]['RCompTest']*100).toFixed(0)+'%';
            }
            if(resultData.jxhAppArray[i]['deptRCompTest']!=''){
                resultData.jxhAppArray[i]['deptRCompTest'] = parseFloat(resultData.jxhAppArray[i]['deptRCompTest']*100).toFixed(0)+'%';
            }
        }


    }
    function getChartsOptions(input,year,months){
        var result = [];
        result.push("已完成设计占比");
        result.push("已完成编码占比");
        result.push("已完成测试占比");

        var option = {
            title:{
                text: year + "年" + months+ "月份版本整体研发进步（百分比）",
                textStyle:{
                    fontSize:23
                },
                x:"center"
            },
            legend:{
                data:result,
                x:"center",
                top:30
            },
            xAxis:{
                type:'category',
                "axisLabel":{interval:0,
                },
                data:getJXHAppNameCol(input),
            },
            dataZoom:{
                type:'slider',
                orient:'horizontal',
                filterMode:"filter",
            },
            yAxis:{
                type:'value',
                data:["0","20%","40%","60%","80%","100%"]
            },
            series:[],
            tooltip:{
                trigger:'axis',
                axisPointer:{type:"cross"}
            }
        };
        var legendArr = ['RCompDetaDesign','RCompCoding','RCompTest'];
        for(var i=0;i<result.length;i++){
            var seriesComponent = {
                name:result[i],
                type:'bar',
                barWidth:'10',
                data:[]
            }
            for(j=0;j<input.length;j++){
                seriesComponent.data.push(parseFloat(input[j][legendArr[i]]));
            }
            option.series.push(seriesComponent);
        }
        return option;

    }
    function getJXHAppNameCol(data){
        var res = [];
        for(var i=0;i<data.length;i++){
            res.push(data[i].appShortName);
        }
        return res;
    }

})