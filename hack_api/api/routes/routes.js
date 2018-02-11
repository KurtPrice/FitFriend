'use strict';
module.exports = function(app) {
  var api = require('../controllers/controller');
  // todoList Routes
  app.route('/users')
    .get(api.list_all_users);

  var AuthController = require('../auth/auth-controller');
  app.use('/api/auth', AuthController);
  
};
