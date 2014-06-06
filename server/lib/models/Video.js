"use strict";

module.exports = function(db) {
	
    var schema = new db.Schema({
      url:  String,
      name: String,
      date_created: { type: Date, default: Date.now },
      popularity: Number,
      tags: String
    }, { collection: 'videos' });

    return db.model('Video', schema);
};
