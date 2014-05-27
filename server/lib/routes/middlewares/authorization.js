'use strict';

var passport = require('passport');


/**
 * Generic require login routing middleware
 */
exports.requiresAuthentication = passport.authenticate('bearer', { session: false });
