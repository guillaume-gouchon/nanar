"use strict";

var authorization = require('./middlewares/authorization');
var videos = require('../controllers/videos');

module.exports.init = function (app) {

	app.get('/videos', authorization.requiresAuthentication, videos.getVideos);

	app.get('/videos/:videoId', authorization.requiresAuthentication, videos.getVideoDetails);

	app.post('/videos', authorization.requiresAuthentication, videos.createVideo);

	app.post('/videos/:videoId/comments', authorization.requiresAuthentication, videos.addComment);

	app.delete('/videos/:videoId/comments/:commentId', authorization.requiresAuthentication, videos.deleteComment);

	app.post('/videos/:videoId/likes', authorization.requiresAuthentication, videos.likeVideo);

	app.get('/videos/url', authorization.requiresAuthentication, videos.getVideoByUrl)

};
