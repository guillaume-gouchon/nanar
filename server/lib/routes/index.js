"use strict";

module.exports.init = function (app) {

	// serve app
	app.get('/', function (req, res, next) {
	    res.send('Server is running !');
	});

	// init routes
	var routes = ['users', 'videos'];

	for (var i = 0; i < routes.length; i++) {
		require('./' + routes[i]).init(app);
	}

}
