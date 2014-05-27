"use strict";

var config = require('../config');
var passport = require('passport');
var BearerStrategy = require('passport-http-bearer').Strategy;

var users = require('./controllers/users');

exports.init = function () {

	// Bearer strategy
	passport.use(new BearerStrategy(
	  function (token, done) {
	  	process.nextTick(function () {
		    users.getByToken(token, function (err, user) {
		      if (err) { return done(err); }
		      if (!user) { return done(null, false); }
		      return done(null, user, user);
		    });
		  });
	  }
	));

};
