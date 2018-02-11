/**
 * Authors: Andy Brown and Dayne Andersen
 * Original functions based off of tutorial:
 * https://www.codementor.io/olatundegaruba/nodejs-restful-apis-in-10-minutes-q0sgsfhbd
 */


var express = require('express'),

    app = express(),
    port = process.env.PORT || 105,
    mongoose = require('mongoose'),
    User = require('./api/models/model'), //created model loading here
    bodyParser = require('body-parser'),
    jwt = require('jsonwebtoken'),
    bcrypt = require('bcryptjs'),
    VerifyToken = require('./verify-token');

// mongoose instance connection url connection
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:3001/db'); 


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


var routes = require('./api/routes/routes'); //importing route
routes(app); //register the route


app.listen(port);

console.log('Starting Server on ' + port);
