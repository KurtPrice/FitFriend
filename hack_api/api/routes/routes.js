'use strict';
module.exports = function(app) {
  var api = require('../controllers/controller');
  // todoList Routes
  app.route('/users')
    .get(api.list_all_users)
    .post(api.create_a_user);

  app.route('/users/:userId')
    .get(api.read_a_user)
    .put(api.update_a_user)
    // .delete(api.delete_a_user);

  app.route('/users/login')
    .get(api.user_login)

  var AuthController = require('../auth/auth-controller');
  app.use('/api/auth', AuthController);
  
};
