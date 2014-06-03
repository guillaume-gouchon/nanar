"use strict";

module.exports = function(db) {
	
    var schema = new db.Schema({
      url:  String,
      name: String,
      date_created: { type: Date, default: Date.now },
      popularity: Number,
      tags: [db.models.Tag.schema]
    }, { collection: 'videos' });

    return db.model('Video', schema);
};
