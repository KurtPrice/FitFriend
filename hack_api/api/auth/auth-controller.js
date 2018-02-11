var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

var VerifyToken = require('../../verify-token');

var mongoose = require('mongoose'),
    User = mongoose.model('Users');
/**
 * Configure JWT
 * Github repo and tutorial source
 * https://medium.freecodecamp.org/securing-node-js-restful-apis-with-json-web-tokens-9f811a92bb52
 */
var jwt = require('jsonwebtoken'); // used to create, sign, and verify tokens
var bcrypt = require('bcryptjs');
var config = require('../../config'); // get config file

router.post('/register', function(req, res) {
  
  var hashedPassword = bcrypt.hashSync(req.body.password, 8);
  
  User.findOne({ email: req.body.email }, function (err, user) {
    if (err) return res.status(500).send('Error on the server.');
    if (user) return res.status(409).send('Email already registered!' + req.body.email);

    User.create({
      name : req.body.name,
      email : req.body.email,
      password : hashedPassword
      //schedule: JSON.parse(req.body.schedule)
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

router.post('/login', function(req, res) {

  User.findOne({ email: req.body.email }, function (err, user) {
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

router.get('/logout', function(req, res) {
  res.status(200).send({ auth: false, token: null });
});

router.get('/me', function(req, res, next) {
  var token = req.headers['x-access-token'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    User.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");
      
      // res.status(200).send(user);
      next(user);
    });
  });
});

router.use(function (user, req, res, next) {
  res.status(200).send(user);
});

//WTF is next()
// router.get('/matching', function(req, res) {
//   var token = req.headers['x-access-token'];
//   if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
//   jwt.verify(token, config.secret, function(err, decoded) {
//     if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
//     User.findById(decoded.id,
//       { password: 0}, 
//       function (err, user) {
//       if (err) return res.status(500).send("There was a problem finding the user.");
//       if (!user) return res.status(404).send("No user found.");
      
//       User.find({
//           {
//             matches: { $in: user.matches}
//           }
      
//       }, function(err, userOther) {
//         if (err)
//           res.send(err);
//         res.json(userOther);
//       });
//     });
//   });
// });

router.put('/update/:userId', function(req, res) {
  var token = req.params.userId;
  // req.body.mon = false
  var stuff = req.body
  req.body.seen = ["cool", "neat"];
  
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    User.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");
      
      User.findOneAndUpdate({_id: user._id}, req.body, {returnNewDocument: true}, function(err, userNew) {
        if (err)
          res.send(err);
        res.json(userNew);
      });
    });
  });
});

router.get('/cards', function(req, res) {
  var token = req.headers['x-access-token'];
  if (!token) return res.status(401).send({ auth: false, message: 'No token provided.' });
  
  jwt.verify(token, config.secret, function(err, decoded) {
    if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
    
    User.findById(decoded.id,
      { password: 0}, 
      function (err, user) {
      if (err) return res.status(500).send("There was a problem finding the user.");
      if (!user) return res.status(404).send("No user found.");

      User.find({
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

          User.findOneAndUpdate({_id: user._id}, {$push: { seen: {$each: others}}}, {returnNewDocument: false}, function(err, userNew) {
            if (err)
              res.send(err);
            
          });
        } 
          
        res.json(userOthers);
        
        
        

      }).limit(10);
    });
  });
});

module.exports = router;