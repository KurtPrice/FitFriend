'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
  name: {
    type: String,
    required: 'Kindly enter the name of the User'
  },
  password:{
    type: String,
    default: "Password123"
  },
  email: {
    type: String,
    default: "NONE"
  },
  bio: {
    type: String,
    default: "EMPTY"
  },
  mon: {
    type: Boolean,
    default: true
  },
  tue: {
    type: Boolean,
    default: true
  },
  wed: {
    type: Boolean,
    default: true
  },
  thu: {
    type: Boolean,
    default: true
  },
  fri: {
    type: Boolean,
    default: true
  },
  sat: {
    type: Boolean,
    default: true
  },
  sun: {
    type: Boolean,
    default: true
  },
  interests: {
    type: Array,
    default: ['F', 'S', 'J', 'W', 'G']
  },
  Created_date: {
    type: Date,
    default: Date.now
  },
  location: {
    type: [Number],
    default: [0.5,0.0]
  },
  status: {
    type: [{
      type: String,
      enum: ['pending', 'ongoing', 'completed']
    }],
    default: ['pending']
  },
  image: {
    type: String,
    default: "https://i.imgur.com/rsD0RUq.jpg"
  },
  likes: {
    type: [String],
    default: ["none"]
  },
  matches: {
    type: [String],
    default: ["none"]
  },
  seen: {
    type: [String],
    default: ["nobody"]
  }
});

var MessageSchema = new Schema({
  name1: {
    type: String,
    required: 'Who is the first user?'
  },
  name2: {
    type: String,
    required: 'Whos is the second user?'
  },
  message: {
    type: String,
    required: 'WHAT WAS THE MESSAGE????'
  },
  date: {
    type: Date,
    default: Date.now
  }
});

module.exports = [mongoose.model('Messages', MessageSchema),
                  mongoose.model('Users', UserSchema)];