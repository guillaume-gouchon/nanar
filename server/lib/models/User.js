"use strict";

module.exports = function(db) {

    var schema = new db.Schema({
      name:  String,
      google_id: String,
      google_token:   String,
      date_created: { type: Date, default: Date.now },
      favorites: [String],
      nb_video_played: { type: Number, default: 0 }
    }, { collection: 'users' });

    return db.model('User', schema);
};
