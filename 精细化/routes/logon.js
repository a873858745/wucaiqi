/**
 * Created by Administrator on 2017/10/27 0027.
 */
var express = require('express');
var router = express.Router();

//载入所需model
var models = require('../utils/jxh_schema');
var PersonInfo = models.PersonInfo;

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('logon------respond');
    console.log('logon------respond');
    //res.render('logon');
});
router.post('/', function(req, res, next) {
    console.log("logon---post---req");
    console.log("logon---post---req----data-----"+JSON.stringify(req.body));
    userAuth(req,res,next);
});
var userAuth = function (req, res, next) {
    console.log("userAuth----");
    var username = req.body.username;
    var password = req.body.password;
    console.log("username:"+username+",password:"+password);
    PersonInfo.find({"username":username,"password":password}).exec(function(err,doc){
        console.log("userAuth----PersonInfo");
        if(err){
            console.log(err);
            res.status(406).end();
        }else if(!doc){
            res.status(406).end();
        }else{
            console.log("doc-------"+doc.length);
            if(doc.length==1){
                res.status(200).send("0");
            }else {
                res.send("1");
            }
        }
    })
    /*if(user=="wucaiqi" && password=="wucaiqi123"){
        console.log("user--password is right...");
        res.status(200).send("0");
    }else{
        res.send("1");
    }*/
}
module.exports = router;
