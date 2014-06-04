'use strict'

var logger = require('winston');

module.exports = {

	dispatch: function (err, req, res, next) {
		var message = err.message;
		var status = err.status || 500;
		if (status == 500) {
			logger.error(err.error || err);
		}
		res.json(status, { message: message });
	},

	Error: function (error, message) {
		this.error = error;
		this.message = message;
		this.status = 500;
	},

	Unauthorized: function (error, message) {
		this.message = message;
		this.status = 401;
	},

	BadRequest: function (error, message) {
		this.message = message;
		this.status = 400;
	}

};
