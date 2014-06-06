'use strict';

var request = require('request');
var logger = require('winston');
var async = require('async');
var Errors = require('../errors');
var Utils = require('../utils');
var db = require('../db');
var Video = db.models.Video;
var Tag = db.models.Tag;


exports.getVideos = function (req, res, next) {
	logger.log('debug', 'Get videos...');

	var pagination = req.query.pagination || 20;
	if (pagination <= 0) { pagination = 20; }
	var page = req.query.page || 1;
	if (page <= 0) { page = 1; }

	var query = {};
	var tag = req.query.tag;
	if (tag != null) {
		query.tags = { 
			label: tag
		}
	}

	Video.find(query)
	.sort({ nb_liked: -1, date_created: -1 })
	.skip(pagination * (page - 1))
	.limit(pagination)
	.exec(function (err, videos) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }

		res.send(videos);
	});
};

exports.getVideoDetails = function (req, res, next) {
	logger.log('debug', 'Get video details...');

	var videoId = req.params.videoId;

  // find or create account
  Video.findOne({ 
		id: videoId
	})
	.exec(function (err, video) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }
		if (video == null) { return next(new Errors.BadRequest(err, 'Video not found')); }
			
		res.send(video);
	});
};

exports.getVideoByUrl = function (req, res, next) {
	// find or create account
  Video.findOne({ 
		url: req.query.url
	})
	.exec(function (err, video) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }
		
		res.send(video);
	});
};

exports.createVideo = function (req, res, next) {
	logger.log('debug', 'Create Video...');

	var url = req.body.url;
	var name = req.body.name;
	var popularity = req.body.popularity;
	var tags = req.body.tags;

  // check that video is unique
  Video.findOne({ 
		url: req.body.url
	})
	.exec(function (err, video) {
		if (err) { return next(new Errors.Error(err, 'Server error')); }
		if (video != null) { return next(new Errors.BadRequest(err, 'Video already exist !')); }

		// add tags to database
		var hTags = tags.split(" ");
		for (var i in hTags) {
			var tagLabel = hTags[i];
			logger.log('debug', 'Adding tag ' + tagLabel);
			if (tagLabel != null && tagLabel.length >= 3) {
				Tag.update({
					label: tagLabel
				}, {
					label: tagLabel
				}, {
					upsert: true
				})
				.exec();
			}
		}

		// create new video
		var video = new Video();
		video.url = url;
		video.name = name;
		video.popularity = popularity;
		video.tags = tags;
		video.save(function (err, video) {
			if (err) { return next(new Errors.Error(err, 'Server error')); }

			res.send(video);
		});
	});
};

// exports.likeVideo = function (req, res, next) {
// 	logger.log('debug', 'Like Video...');

// 	var videoId = req.params.videoId;
// 	var userId = req.authInfo._id;

//   // find or create account
//   Video.findOne({ 
// 		id: videoId
// 	})
// 	.exec(function (err, video) {
// 		if (err) { return next(new Errors.Error(err, 'Server error')); }
// 		if (video == null) { return next(new Errors.BadRequest(err, 'Video not found')); }
// 		if (video.likes.indexOf(userId) > 0) { return next(new Errors.BadRequest(err, 'Video already liked !')); }

// 		video.nb_likes++;
// 		video.likes.push(userId);
// 		video.save(function (err, video) {
// 			if (err) { return next(new Errors.Error(err, 'Server error')); }

// 			res.send(200);
// 		});
// 	});
// };

// exports.addComment = function (req, res, next) {
// 	logger.log('debug', 'Add Comment...');

// 	var userId = req.authInfo._id;
// 	var text = req.body.text;

//   // check video is unique
//   Video.findOne({ 
// 		id: req.params.videoId
// 	})
// 	.exec(function (err, video) {
// 		if (err) { return next(new Errors.Error(err, 'Server error')); }
// 		if (video == null) { return next(new Errors.BadRequest(err, 'Video not found')); }

// 		// create new comment and add it to the video
// 		var comment = new Comment();
// 		comment.user_id = userId;
// 		comment.text = text;
// 		video.comments.push(comment);
// 		video.save(function (err, comment) {
// 			if (err) { return next(new Errors.Error(err, 'Server error')); }

// 			res.send(200);
// 		});
// 	});
// };

// exports.deleteComment = function (req, res, next) {
// 	logger.log('debug', 'Delete Comment...');

// 	var userId = req.authInfo._id;
// 	var text = req.body.text;

//   // check video is unique
//   Video.update({
//   }, 
// 	{
// 		$pull: {
// 			comments: {
// 				id: req.params.commentId,
// 				user_id: userId,
// 			}
// 		}
// 	})
// 	.exec(function (err) {
// 		if (err) { return next(new Errors.Error(err, 'Server error')); }

// 		res.send(200);
// 	});
// };
