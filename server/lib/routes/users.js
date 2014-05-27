"use strict";

var authorization = require('./middlewares/authorization');
var users = require('../controllers/users');


module.exports.init = function (app) {

	app.post('/login', users.login);
	
	app.get('/users/:userId', authorization.requiresAuthentication, users.getUser);

	app.put('/users/:userId', authorization.requiresAuthentication, users.updateUser);

};