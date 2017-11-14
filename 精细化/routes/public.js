/**
 * Created by wucq on 2017/10/27 0027.
 */
var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('home');
    console.log("public.js--------");
});
router.post('/', function(req, res, next) {
    res.render('home');
});

module.exports = router;

