"use strict";

var authorization = require('./middlewares/authorization');
var videos = require('../controllers/videos');

module.exports.init = function (app) {

	app.get('/videos', videos.getVideos);

	app.get('/videos/:videoId', videos.getVideoDetails);

	app.post('/videos', videos.createVideo);

	app.get('/videos/url', authorization.requiresAuthentication, videos.getVideoByUrl)

};
