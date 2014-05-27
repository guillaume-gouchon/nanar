"use strict";

var logger = require('winston');
var gcm = require('node-gcm');
var config = require('../config');


String.prototype.replaceAll = function (find, replace) {
	var str = this;
  return str.replace(new RegExp(find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), replace);
}

module.exports = {

	sendGCM: function (messageData, clientId, callback) {
		var message = new gcm.Message({
	    collapseKey: 'demo',
	    delayWhileIdle: true,
	    timeToLive: 3,
	    data: messageData
		});

		var sender = new gcm.Sender(config.server.gcm.key);
		sender.send(message, [clientId], 4, callback);
	}

};
