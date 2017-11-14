/**
 * Created by Administrator on 2017/10/27 0027.
 */
var express = require('express');
var async = require("async");
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});
//载入所需model
var models = require("../utils/jxh_schema");
var RefAppOld = models.RefAppOld;
var RefDeptOld = models.RefDeptOld;
var RefInfo = models.RefInfo;



router.post("/queryJXHAppAll", function (req, res) {
    var year = "";
    var month = "";
    //查询更新时间
    var queryLastUpdate = function (callback) {
        console.log("queryJXHAppAll---queryLastUpdate")
        RefInfo.findOne({paraname:"lastCreateTime"},{_id:0},function(err,doc){
            if(err){
                console.error(err);
                res.status(406).end();
            }else if(!doc){
                res.status(406).end();
            }else{
                var currentState = doc.toJSON();
                var lastupdate = currentState.paravalue.toString();
                console.log("queryJXHAppAll---queryLastUpdate--lastupdate"+lastupdate)
                callback(null,lastupdate);
            }
        });
    };
    //查询应用维度数据
    var queryData = function (lastupdate, callback) {
        year = parseInt(lastupdate.toString().substring(0,4));
        month = parseInt(lastupdate.toString().substring(4,6));
        var jxhAppResult = {};
        var jxhAppArray = new Array();
        console.log("queryJXHAppAll---queryData--lastupdate"+lastupdate)
        RefAppOld.find({version:year+"年"+month+"月份版本",createTime:lastupdate}).sort({version:1,"ADLMWlColl":-1})
            .exec(function (err, doc) {
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhAppArray.push(docJson);
                    }
                    jxhAppResult.jxhAppArray = jxhAppArray;
                    jxhAppResult.lastupdate = lastupdate;
                    callback(null,jxhAppResult);
                }
            })
    };
    //查询部门维度数据
    var queryDataAppDept = function (jxhAppResult, callback) {
        var jxhAppDeptResult = {};
        var jxhDeptArray = new Array();
        var lastupdate = jxhAppResult.lastupdate;
        var jxhAppArray = jxhAppResult.jxhAppArray;
        RefDeptOld.find({version:year+"年"+month+"月份版本",createTime:lastupdate}).sort({version:1,dept:1})
            .exec(function (err, doc) {
                console.log("queryJXHAppAll---queryDataAppDept--RefDeptOld")
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhDeptArray.push(docJson);
                    }
                    for(var i=0;i<jxhAppArray.length;i++){
                        for(var j=0;j<jxhDeptArray.length;j++){
                            if(jxhAppArray[i].version==jxhDeptArray[j].version && jxhAppArray[i].dept==jxhDeptArray[j].dept){
                                jxhAppArray[i].deptRCompDemaAnalWl= jxhDeptArray[j].RCompDemaAnalWl;
                                jxhAppArray[i].deptRCompDetaDesign = jxhDeptArray[j].RCompDetaDesign;
                                jxhAppArray[i].deptRCompCoding = jxhDeptArray[j].RCompCoding;
                                jxhAppArray[i].deptRCompTest = jxhDeptArray[j].RCompTest;
                            }
                        }
                    }
                    console.log("year:"+year+"----month:"+month);
                    jxhAppDeptResult.year = year;
                    jxhAppDeptResult.month = month;
                    jxhAppDeptResult.lastupdate = lastupdate;
                    jxhAppDeptResult.jxhAppArray = jxhAppArray;
                    console.log("jxhAppDeptResult---"+JSON.stringify(jxhAppDeptResult))
                    callback(null,jxhAppDeptResult);
                }
            })
    };
    //将上述三个函数串行执行
    async.waterfall([queryLastUpdate,queryData,queryDataAppDept],function(err,result){
        if(err){
            res.status(406).end();
        }else{
            res.json(result);
        }
    })
})
router.post("/queryAppDataByVersion",function(req,res){
    var year = req.body.year;
    var dept = req.body.dept;
    var choosedVersionArr = req.body.choosedVersionArr;
    var queryLastUpdate = function (callback) {
        console.log("queryAppDataByVersion---queryLastUpdate")
        RefInfo.findOne({paraname:"lastCreateTime"},{_id:0},function(err,doc){
            if(err){
                console.error(err);
                res.status(406).end();
            }else if(!doc){
                res.status(406).end();
            }else{
                var currentState = doc.toJSON();
                var lastupdate = currentState.paravalue.toString();
                console.log("queryAppDataByVersion---queryLastUpdate--lastupdate"+lastupdate)
                callback(null,lastupdate);
            }
        });
    };
    var queryData = function (lastupdate, callback) {
        var jxhAppResult = {};
        var jxhAppArray = new Array();
        var whereStr = {};
        var versionArray = new Array();
        for(i=0;i<choosedVersionArr.length;i++){
            var version = year+"年"+choosedVersionArr[i]+"月份版本";
            versionArray.push(version);
        }
        if(dept=="所有部门"){
            whereStr = {$or:[{"version":{$in:versionArray}}],createTime:{$regex:lastupdate}};
        }else{
            whereStr = {$or:[{"version":{$in:versionArray}}],dept:dept,createTime:{$regex:lastupdate}};
        }
        RefAppOld.find(whereStr).sort({version:1,"ADLMWlColl":-1})
            .exec(function (err, doc) {
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhAppArray.push(docJson);
                    }
                    jxhAppResult.jxhAppArray = jxhAppArray;
                    jxhAppResult.lastupdate = lastupdate;
                    callback(null,jxhAppResult);
                }
            })
    };
    var queryDataAppDept = function (jxhAppResult, callback) {
        var jxhAppDeptResult = {};
        var jxhDeptArray = new Array();
        var lastupdate = jxhAppResult.lastupdate;
        var jxhAppArray = jxhAppResult.jxhAppArray;
        var whereStr = {};
        var versionArray = new Array();
        for(i=0;i<choosedVersionArr.length;i++){
            var version = year+"年"+choosedVersionArr[i]+"月份版本";
            versionArray.push(version);
        }
        if(dept=="所有部门"){
            whereStr = {$or:[{"version":{$in:versionArray}}],createTime:{$regex:lastupdate}};
        }else{
            whereStr = {$or:[{"version":{$in:versionArray}}],dept:dept,createTime:{$regex:lastupdate}};
        }
        RefDeptOld.find(whereStr).sort({version:1,dept:1})
            .exec(function (err, doc) {
                //console.log("queryJXHAppAll---queryDataAppDept--RefDeptOld")
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhDeptArray.push(docJson);
                    }
                    for(var i=0;i<jxhAppArray.length;i++){
                        for(var j=0;j<jxhDeptArray.length;j++){
                            if(jxhAppArray[i].version==jxhDeptArray[j].version && jxhAppArray[i].dept==jxhDeptArray[j].dept){
                                jxhAppArray[i].deptRCompDemaAnalWl= jxhDeptArray[j].RCompDemaAnalWl;
                                jxhAppArray[i].deptRCompDetaDesign = jxhDeptArray[j].RCompDetaDesign;
                                jxhAppArray[i].deptRCompCoding = jxhDeptArray[j].RCompCoding;
                                jxhAppArray[i].deptRCompTest = jxhDeptArray[j].RCompTest;
                            }
                        }
                    }
                    jxhAppDeptResult.year = year;
                    jxhAppDeptResult.month = choosedVersionArr;
                    jxhAppDeptResult.lastupdate = lastupdate;
                    jxhAppDeptResult.jxhAppArray = jxhAppArray;
                    console.log("jxhAppDeptResult---"+JSON.stringify(jxhAppDeptResult))
                    callback(null,jxhAppDeptResult);
                }
            })
    };
    //将上述三个函数串行执行
    async.waterfall([queryLastUpdate,queryData,queryDataAppDept],function(err,result){
        if(err){
            res.status(406).end();
        }else{
            res.json(result);
        }
    })
})
router.post("/queryJXHAppDataByApp",function(req,res){
    var appName = req.body.option;
    var year = req.body.year;
    var dept = req.body.dept;
    var choosedVersionArr = req.body.choosedVersionArr;
    var queryLastUpdate = function (callback) {
        console.log("queryAppDataByVersion---queryLastUpdate")
        RefInfo.findOne({paraname:"lastCreateTime"},{_id:0},function(err,doc){
            if(err){
                console.error(err);
                res.status(406).end();
            }else if(!doc){
                res.status(406).end();
            }else{
                var currentState = doc.toJSON();
                var lastupdate = currentState.paravalue.toString();
                console.log("queryAppDataByVersion---queryLastUpdate--lastupdate"+lastupdate)
                callback(null,lastupdate);
            }
        });
    };
    var queryData = function (lastupdate, callback) {
        var jxhAppResult = {};
        var jxhAppArray = new Array();
        var whereStr = {};
        var versionArray = new Array();
        for(i=0;i<choosedVersionArr.length;i++){
            var version = year+"年"+choosedVersionArr[i]+"月份版本";
            versionArray.push(version);
        }
        if(dept=="所有部门"){
            whereStr = {$or:[{"version":{$in:versionArray}}],appShortName:appName,createTime:{$regex:lastupdate}};
        }else{
            whereStr = {$or:[{"version":{$in:versionArray}}],dept:dept,appShortName:appName,createTime:{$regex:lastupdate}};
        }
        console.log("queryJXHAppDataByApp---whereStr-----"+whereStr);
        RefAppOld.find(whereStr).sort({version:1,"ADLMWlColl":-1})
            .exec(function (err, doc) {
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhAppArray.push(docJson);
                    }
                    jxhAppResult.jxhAppArray = jxhAppArray;
                    jxhAppResult.lastupdate = lastupdate;
                    callback(null,jxhAppResult);
                }
            })
    };
    var queryDataAppDept = function (jxhAppResult, callback) {
        var jxhAppDeptResult = {};
        var jxhDeptArray = new Array();
        var lastupdate = jxhAppResult.lastupdate;
        var jxhAppArray = jxhAppResult.jxhAppArray;
        var whereStr = {};
        var versionArray = new Array();
        for(i=0;i<choosedVersionArr.length;i++){
            var version = year+"年"+choosedVersionArr[i]+"月份版本";
            versionArray.push(version);
        }
        if(dept=="所有部门"){
            whereStr = {$or:[{"version":{$in:versionArray}}],createTime:{$regex:lastupdate}};
        }else{
            whereStr = {$or:[{"version":{$in:versionArray}}],dept:dept,createTime:{$regex:lastupdate}};
        }
        RefDeptOld.find(whereStr).sort({version:1,dept:1})
            .exec(function (err, doc) {
                //console.log("queryJXHAppAll---queryDataAppDept--RefDeptOld")
                if(err){
                    console.error(err);
                    res.status(406).end();
                }else if(!doc){
                    res.status(406).end();
                }else{
                    for(var i=0;i<doc.length;i++){
                        var docJson = doc[i].toJSON();
                        jxhDeptArray.push(docJson);
                    }
                    for(var i=0;i<jxhAppArray.length;i++){
                        for(var j=0;j<jxhDeptArray.length;j++){
                            if(jxhAppArray[i].version==jxhDeptArray[j].version && jxhAppArray[i].dept==jxhDeptArray[j].dept){
                                jxhAppArray[i].deptRCompDemaAnalWl= jxhDeptArray[j].RCompDemaAnalWl;
                                jxhAppArray[i].deptRCompDetaDesign = jxhDeptArray[j].RCompDetaDesign;
                                jxhAppArray[i].deptRCompCoding = jxhDeptArray[j].RCompCoding;
                                jxhAppArray[i].deptRCompTest = jxhDeptArray[j].RCompTest;
                            }
                        }
                    }
                    jxhAppDeptResult.year = year;
                    jxhAppDeptResult.month = choosedVersionArr;
                    jxhAppDeptResult.lastupdate = lastupdate;
                    jxhAppDeptResult.jxhAppArray = jxhAppArray;
                    console.log("jxhAppDeptResult---"+JSON.stringify(jxhAppDeptResult))
                    callback(null,jxhAppDeptResult);
                }
            })
    };
    //将上述三个函数串行执行
    async.waterfall([queryLastUpdate,queryData,queryDataAppDept],function(err,result){
        if(err){
            res.status(406).end();
        }else{
            res.json(result);
        }
    })
})


module.exports = router;
