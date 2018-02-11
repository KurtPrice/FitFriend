'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
  name: {
    type: String,
    required: 'Kindly enter the name of the User'
  },
  email: {
    type: String,
    default: "NONE"
  },
  bio: {
    type: String,
    default: "EMPTY"
  },
  schedule: {
    type: [Boolean],
    default: [0, 0, 0, 0, 0, 0, 0]
  },
  token: {
    type: String,
    default: "null"
  },
  interests: {
    type: Array,
    default: []
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
  }
});

module.exports = mongoose.model('Users', UserSchema);