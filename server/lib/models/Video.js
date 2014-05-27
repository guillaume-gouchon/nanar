"use strict";

module.exports = function(db) {
	
    var schema = new db.Schema({
      url:  String,
      name: String,
      movie_name: String,
      user_id: String,
      date_created: { type: Date, default: Date.now },
      likes: [String],
      nb_likes: { type: Number, default: 0 },
      comments: [db.models.Comment.schema],
      tags: [db.models.Tag.schema]
    }, { collection: 'videos' });

    return db.model('Video', schema);
};
