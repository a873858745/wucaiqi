/**
 * Created by Administrator on 2017/11/2 0002.
 */
var constants = require('./constants_utils');
var mongoose = require("mongoose");
mongoose.Promise = require('q').Promise;
//mongoose.connect(constants.MONGODB_CONNECT.uri);
var promise = mongoose.connect(constants.MONGODB_CONNECT.uri,{
    useMongoClient:true
});
promise.then(function () {
    console.log("Mongodb is connected");
},function(err){
    console.log(err);
})

/*
mongoose.connection.on('error', function (err) {
    console.log(err);
});
mongoose.connection.once('open', function () {
    console.log("Mongodb id connected");
})*/
