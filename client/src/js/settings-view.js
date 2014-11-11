
define(function (require) {
    'use strict';

    var Q = require('q-xhr');
    var NavBar = require('js/nav-bar');
    var settingsTmpl = require('js/templates/settings.tmpl');

    function SettingsView($container) {
        this.$container = $container;
        this.nav = null;
        this.user = null;
    }

    SettingsView.prototype.init = function () {
        var self = this;

        var data = {
            settingsLabel: 'Settings',
            nameLabel: 'Name',
            nicknameLabel: 'Nickname',
            submitLabel: 'Submit',
        };

        this.$container.append(settingsTmpl(data));

        Q.xhr.get('/api/v1/users/current').then(function (resp) {
            self.setUser(resp.data);
        });
    };

    SettingsView.prototype.setUser = function (user) {
        this.user = user;
        this.nav = new NavBar(user);
        this.nav.render();
    };

    return SettingsView;
});
