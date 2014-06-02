"use strict";

var authorization = require('./middlewares/authorization');
var tags = require('../controllers/tags');


module.exports.init = function (app) {
	
	app.get('/tags', tags.getTags);

};