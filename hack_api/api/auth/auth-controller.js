/**
 * Authetication based 
 * Original functions based off of Github repo and tutorial:
 * https://medium.freecodecamp.org/securing-node-js-restful-apis-with-json-web-tokens-9f811a92bb52
 */

var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

var VerifyToken = require('../../verify-token');

var mongoose = require('mongoose'),
    UserDB = mongoose.model('Users'),
    MessageDB = mongoose.model('Messages');

var jwt = require('jsonwebtoken'); // used to create, sign, and verify tokens
var bcrypt = require('bcryptjs');
var config = require('../../config'); // get config file

/* Register new user
 * Requires body.{name, email, password}
 * Returns session Token when a user is successfully created
 */
router.post('/register', function(req, res) {
  
  console.log("New User Request!");
  var hashedPassword = bcrypt.hashSync(req.body.password, 8);
  
  UserDB.findOne({ email: req.body.email }, function (err, user) {
    if (err) return res.status(500).send('Error on the server.');
    if (user) return res.status(409).send('Email already registered!' + req.body.email);

    UserDB.create({
      name : req.body.name,
      email : req.body.email,
      password : hashedPassword
    },
    function (err, user) {
      if (err) return res.status(500).send("There was a problem registering the user.")
      // create a token
      var token = jwt.sign({ id: user._id }, config.secret, {
        expiresIn: 86400 // expires in 24 hours
      });
      res.status(200).send({ auth: true, token: token });
    }); 
  });
});

/* Login user
 * Requires body.{email, password}
 * Returns authentication results and session Token
 */
router.post('/login', function(req, res) {
  console.log("Login Request!");
  UserDB.findOne({ email: req.body.email }, function (err, user) {
    if (err) return res.status(500).send('Error on the server.');
    if (!user) return res.status(404).send('No user found.');
    
    // check if the password is valid
    var passwordIsValid = bcrypt.compareSync(req.body.password, user.password);
    if (!passwordIsValid) return res.status(401).send({ auth: false, token: null });

    // if user is found and password is valid
    // create a token
    var token = jwt.sign({ id: user._id }, config.secret, {
      expiresIn: 86400 // expires in 24 hours
    });

    // return the information including token as JSON
    res.status(200).send({ auth: true, token: token });
  });

});



/* Request your own user data
 * Requires headers.x-access-token
 * Returns your user object
 */
router.get('/me', function(req, res, next) {
  
  console.log("Self Request!");
  var token = req.headers['x-access-token'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");
      
      res.status(200).send(user);
    });
  });
});


/* Update user information
 * Requires token appended to url request
 * Updates fields provided in body.
 * Returns your user object
 */
router.put('/update/:userId', function(req, res) {
  
  console.log("Update Request!");
  var token = req.params.userId;
  // req.body.mon = false
  var stuff = req.body
  
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");
      
      UserDB.findOneAndUpdate({_id: user._id}, req.body, {returnNewDocument: true}, function(err, userNew) {
        if (err)
          res.send(err);
        res.json(userNew);
      });
    });
  });
});


/* Request a set of new users
 * Requires header.x-access-token
 * New users must have matching interests and schedule availability
 * Once a user has been fetched, it will not be returned on subsequent requests
 * Returns an array of users
 */
router.get('/cards', function(req, res) {
  var token = req.headers['x-access-token'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");

      UserDB.find({
        $and : [
          {
            $or : [ { 
              $and : [
                {mon: true},
                {mon: user.mon}
              ],
              $and : [
                {tue: true},
                {tue: user.tue}
              ],
              $and : [
                {wed: true},
                {wed: user.wed}
              ],
              $and : [
                {thu: true},
                {thu: user.thu}
              ],
              $and : [
                {fri: true},
                {fri: user.fri}
              ],
              $and : [
                {sat: true},
                {sat: user.sat}
              ],
              $and : [
                {sun: true},
                {sun: user.sun}
              ]
            }]
          }, 
          {
            interests: { $in: user.interests}
          },
          {
            email: { $nin: user.seen}
          }
        ]
      }, function(err, userOthers) {
        if (err)
          res.send(err);
        if (userOthers != null && userOthers.length > 0) {
          var others = []
          for (var i = 0;i<userOthers.length;i++){
            others.push(userOthers[i].email)
          }

          UserDB.findOneAndUpdate({_id: user._id}, {$push: { seen: {$each: others}}}, {returnNewDocument: false}, function(err, userNew) {
            if (err)
              res.send(err);
          });
        } 
          
        res.json(userOthers);
      }).limit(10);
    });
  });
});

/* Likes a user
 * Requires token appended to url request and body.liked of type ._id
 * Updates like 
 * Returns your user object
 */
router.put('/like/:userId', function(req, res) {
  var token = req.params.userId;
  // req.body.mon = false
  var stuff = req.body
  
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });

  console.log("USER: " + token + " Liked USER")
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");
      
      console.log("Liking");

      UserDB.findById(req.body.liked,{ password: 0}, 
        function (err, userLiked) {
        if (err) return res.status(500).send("There was a problem finding the user.");


        
        UserDB.findOneAndUpdate({_id: userLiked}, {$push: { likes: req.body.liked}}, {returnNewDocument: false}, function(err, userNew) {
          if (err)
            res.send(err);
        });

        if (userLiked != null && userLiked.likes != null) {
          console.log("Found!");
          console.log(userLiked.likes);
          console.log(user._id);
          if (user.likes.includes(String(userLiked._id))) {
            
            UserDB.findOneAndUpdate({_id: user._id}, {$push: { matches: req.body.liked}}, {returnNewDocument: false}, function(err, userNew) {
              if (err)
                res.send(err);
            });
            
            UserDB.findOneAndUpdate({_id: userLiked._id}, {$push: { matches: user._id}}, {returnNewDocument: false}, function(err, userNew) {
              if (err)
                res.send(err);
              
            });
          }
        }

        res.status(200).send("You liked user " + req.body.liked);
      });

    });
  });
});

router.post('/messages', function(req, res) {
  var userOther = req.body['user2'];
  var token = req.body['x-access-token'];
  var message = req.body['message'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  if (!userOther) return res.status(401).send({ auth: false, message: 'No user2 provided.'});
  if (!message) return res.status(401).send({ auth: false, message: 'No message provided.'});
  console.log("Message Post!");

  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");

      MessageDB.create({
        name1 : String(user._id),
        name2 : String(userOther),
        message : message
        //schedule: JSON.parse(req.body.schedule)
      },
      function (err, user) {
        if (err) return res.status(500).send("There was a problem creating the message");
        // create a token
        res.status(200).send("Message was recorded");
      }); 


    });
  });
})

router.get('/messages', function(req, res) {
  var userOther = req.headers['user2'];
  var token = req.headers['x-access-token'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  if (!userOther) return res.status(401).send({ auth: false, message: 'No user2 provided.'});
  console.log("Message Request!");

  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    UserDB.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");

      console.log("Verified user!");
      console.log("Other user: " + userOther)
      console.log("Self user:" + user._id)
      MessageDB.find({$and: [{name1: String(user._id)}, {name2: String(userOther)}]}).exec(function(err, messages) {
        if (err) return res.status(500).send("There was a problem finding messages for user");
        console.log(messages);
        res.send(messages);
      });
    });
  });
});



router.get('/lookup', function(req, res) {
  var userOther = req.headers['user'];
  if (!userOther) return res.status(401).send({ auth: false, message: 'No user provided.'});
  console.log("Lookup Request!");

  UserDB.findById(userOther,
    { password: 0}, 
    function (err, user) {
    if (err) return res.status(500).send("There was a problem finding the user.");
    if (!user) return res.status(404).send("No user found.");

    console.log("Found other user!");
    console.log("Other user: " + userOther)
    res.send(user);
  });
});

module.exports = router;