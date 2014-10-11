
define(function (require) {
    'use strict';

    var $ = require('jquery');
    var Q = require('q-xhr');
    var navTmpl = require('js/templates/page-nav.tmpl');

    function ListView($container) {
        this.$container = $container;
        this.$nav = null;
        this.user = null;
    }

    ListView.prototype.init = function () {
        var self = this;
        Q.xhr.get('/api/v1/users/current').then(function (resp) {
            self.setUser(resp.data);
        });
    };

    ListView.prototype.setUser = function (user) {
        this.user = user;
        if (this.$nav) {
            this.$nav.remove();
        }

        this.$nav = $(navTmpl({
            links: [{
                active: true,
                url: '/',
                name: 'Lists',
            }, {
                active: false,
                url: '/groups',
                name: 'Groups',
            }],
            settingsLabel: 'Settings',
            requestsLabel: 'Requests',
            logoutLabel: 'Logout',
            user: this.user,
        }));

        this.$container.append(this.$nav);
    }; // setUser //

    return ListView;
});
