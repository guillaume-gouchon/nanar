"use strict";

module.exports.init = function(db) {

	// load models
	var models = ['Comment', 'Tag', 'Video', 'User'];

	models.forEach(function (model) {
	   require('./' + model)(db);
	});

};
