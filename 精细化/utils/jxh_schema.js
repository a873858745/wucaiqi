/**
 * Created by Administrator on 2017/11/2 0002.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

//精细化
//个人登录信息
var personinfo = new Schema({
    username:String,
    password:String
});
var PersonInfo = mongoose.model("PersonInfo",personinfo);
module.exports.PersonInfo = PersonInfo;

//更新时间表结构
var refinfo = new Schema({
    paraname:String,
    paravalue:String

});
var RefInfo = mongoose.model("RefInfo",refinfo);
module.exports.RefInfo = RefInfo;
//应用维度表结构
var refappold = new Schema({
    version:String,//版本
    dept:String,//部门
    appShortName:String,//应用
    ADLMWlColl:String,//ADLM合计
    ImpoWl:String,//已导入工作量
    RImpoWl:String,//已导入工作量占比
    RCompDemaAnalWl:String,//已完成需求分析占比
    RCompDetaDesign:String,//已完成详细设计占比
    RCompCoding:String,//已完成编码占比
    RCompTest:String,//已完成测试占比
});
var RefAppOld = mongoose.model("RefAppOld",refappold);
module.exports.RefAppOld = RefAppOld;

//部门维度表结构
var refdeptold = new Schema({
    version:String,//版本
    dept:String,//部门
    ADLMWlColl:String,//ADLM合计
    ImpoWl:String,//已导入工作量
    RImpoWl:String,//已导入工作量占比
    RCompDemaAnalWl:String,//已完成需求分析占比
    RCompDetaDesign:String,//已完成详细设计占比
    RCompCoding:String,//已完成编码占比
    RCompTest:String,//已完成测试占比
});
var RefDeptOld = mongoose.model("RefDeptOld",refdeptold);
module.exports.RefDeptOld = RefDeptOld;