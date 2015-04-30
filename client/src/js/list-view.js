
define(function (require) {
    'use strict';

    var Q = require('q-xhr');
    var NavBar = require('js/nav-bar');
    var listsTmpl = require('js/templates/lists.tmpl');

    function ListView($container) {
        this.$container = $container;
        this.nav = null;
        this.user = null;
    }

    ListView.prototype.init = function () {
        var self = this;

        var data = {
            listsLabel: 'Lists',
        };

        this.$container.append(listsTmpl(data));

        Q.xhr.get('/api/v1/users/current').then(function (resp) {
            self.setUser(resp.data);
        });
    };

    ListView.prototype.setUser = function (user) {
        this.user = user;
        this.nav = new NavBar(user);
        this.nav.render();
    };

    return ListView;
});
