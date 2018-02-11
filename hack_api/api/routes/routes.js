/**
 * Authors: Andy Brown and Dayne Andersen
 * Original functions based off of tutorial:
 * https://www.codementor.io/olatundegaruba/nodejs-restful-apis-in-10-minutes-q0sgsfhbd
 */

'use strict';
module.exports = function(app) {
  var api = require('../controllers/controller');
  // todoList Routes
  app.route('/users')
    .get(api.list_all_users);

  var AuthController = require('../auth/auth-controller');
  app.use('/api/auth', AuthController);
  
};
