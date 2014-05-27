'use strict';

var logger = require('winston');
var Errors = require('../errors');
var db = require('../db');
var User = db.models.User;


exports.login = function (req, res, next) {
	logger.log('debug', 'User is logging in...');

	var name = req.body.name;
	var googleId = req.body.google_id;
	var googleToken = req.body.google_token;

	if (!googleId || !googleToken) return next(new Errors.BadRequest('Missing fields'));

  // find or create account
  User.findOne({ 
		google_id: googleId,
		google_token: googleToken
	})
	.exec(function (err, user) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }

		if (user == null) {
			// create new account
			logger.log('debug', 'Creating new account...');
			var user = new User({ 
	      name: name,
	      google_id: googleId,
	      google_token: googleToken
	    })
	    .save(function (err, newUser) {
	    	if (err) { return next(new Errors.Error(err, 'Server error')); }

	    	res.send(200);
	    });
		} else {
			// return existing account
			logger.log('debug', 'Account existed already...');
			user.google_token = null;
			res.send(user);
		}
	});
};

exports.getUser = function (req, res, next) {
	logger.log('debug', 'Get user profile...');

  this.getById(req.params.userId, function (err, user) {
  	if (err) return next(new Errors.Error(err, 'Server error'));
		if (user == null) return next(new Errors.BadRequest('User not found'));

		user.google_token = null;
		res.send(user);
  });
};

exports.updateUser = function (req, res, next) {
	logger.log('debug', 'User is updating its account...');

	var googleToken = req.body.google_token;

  // find account
  User.findOne({ 
		google_token: req.authInfo.google_token
	})
	.exec(function (err, user) {
		if (err) return next(new Errors.Error(err, 'Server error'));
		if (user == null) return next(new Errors.BadRequest('Missing fields'));

		// update account
		if (googleToken != null && googleToken.length > 0) user.google_token = googleToken;

		user.save(function (err, newUser) {
    	if (err) { return next(new Errors.Error(err, 'Server error')); }

    	res.send(200);
    });
	});

};

exports.getById = function (id, callback) {
	logger.log('debug', 'Get user by id...');
	User.findOne({ 
		_id: id
	})
	.exec(callback);
};

exports.getByToken = function (token, callback) {
	logger.log('debug', 'Get user by token...');
	User.findOne({ 
		google_token: token
	})
	.exec(callback);
};
