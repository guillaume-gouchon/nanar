"use strict";

module.exports = function(db) {
	
    var schema = new db.Schema({
      label:  String
    }, { collection: 'tags' });

    return db.model('Tag', schema);
};
