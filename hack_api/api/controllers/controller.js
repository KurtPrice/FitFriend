/**
 * Authors: Andy Brown and Dayne Andersen
 * Original functions based off of tutorial:
 * https://www.codementor.io/olatundegaruba/nodejs-restful-apis-in-10-minutes-q0sgsfhbd
 */

'use strict';


var mongoose = require('mongoose'),
    user = mongoose.model('Users');

exports.list_all_users = function(req, res) {
  user.find({}, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};

exports.create_a_user = function(req, res) {
  var new_user = new user(req.body);
  new_user.save(function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.read_a_user = function(req, res) {
  user.findById(req.params.userId, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};

//INSERT SECURITY BELOW
exports.user_login = function(req, res) {
  user.findOne({'name': req.params.user, 'password': req.params.password}, 'name bio schedule key interests', function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.update_a_user = function(req, res) {
  user.findOneAndUpdate({_id: req.params.userId}, req.body, {new: true}, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.delete_a_user = function(req, res) {
  user.remove({
    _id: req.params.userId
  }, function(err, user) {
    if (err)
      res.send(err);
    res.json({ message: 'user successfully deleted' });
  });
};
