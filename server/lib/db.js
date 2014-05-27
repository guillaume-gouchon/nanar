"use strict";

var mongoose = require('mongoose');
var logger = require('winston');
var config = require('../config');

// init db connection
mongoose.connect('mongodb://' + config.db.host + '/' + config.db.name);

var db = mongoose.connection;

db.on('error', console.error.bind(console, 'Database connection error:'));

db.once('open', function callback () {
  logger.info("Database initialized successfully !");
});

module.exports = mongoose;
