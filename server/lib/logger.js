'use strict'

var config = require('../config');

var winston = require('winston');
winston.remove(winston.transports.Console);

// console logs
winston.add(winston.transports.Console, { level: config.server.logs.console, colorize: true });

// file logs
winston.add(winston.transports.File, { level: config.server.logs.file, 'timestamp': true, filename: config.server.logs.fileName });


module.exports = winston;
