//expressjs
var express = require('express');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var compress = require('compression');
//http server
var http = require('http');
var cluster = require('cluster');
var app = express();

//compress all requests
app.use(compress({
  threshold:'1kb'
}));

var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use('/bower',express.static(path.join(__dirname,'bower_components')));
var defaultRouter = require('./routes/public');
var mongodb = require("./utils/mongodb");
var logon = require('./routes/logon');

//var index = require('./routes/index');
//var users = require('./routes/users');

app.use('/',defaultRouter);
app.use('/logon',logon);
console.log("logon.js is loaded....");
//app.use('/index',index);
//app.use('/users',users);

var dbservice= require('./routes/db_service');
app.use('/dbservice',dbservice);
console.log("dbservice is loaded........");
var count = require('os').cpus().length;


/*app.use('/', index);
app.use('/users', users);*/

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
