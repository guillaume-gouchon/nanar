'use strict';

var request = require('request');
var logger = require('winston');
var async = require('async');
var Errors = require('../errors');
var Utils = require('../utils');
var db = require('../db');
var Tag = db.models.Tag;


exports.getTags = function (req, res, next) {
	logger.log('debug', 'Get tags...');

	Tag.find().exec(function (err, tags) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }

		res.send(tags);
	});
};
