"use strict";

module.exports = function(db) {
	
    var schema = new db.Schema({
      text:  String,
      user_id: String,
      date_created: { type: Date, default: Date.now }
    }, { collection: 'comments' });

    return db.model('Comment', schema);
};
