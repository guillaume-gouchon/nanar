'use strict';

var express = require('express');
var passport = require('passport');
var config = require('./config');
var errors = require('./lib/errors');
var logger = require('./lib/logger');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

// setup database
var db = require('./lib/db');

// setup models
require('./lib/models').init(db);

// setup express server
var app = express();
app.use(cookieParser());
app.use(bodyParser.json({limit: '10mb'}));
app.use(bodyParser.urlencoded({limit: '10mb'}));
app.use(passport.initialize());

// setup routes
require('./lib/routes').init(app);

// error handling middleware
app.use(errors.dispatch);

// setup security
require('./lib/security').init();

// start server
app.listen(config.server.port);
logger.info('Server started on port: ' + config.server.port);
