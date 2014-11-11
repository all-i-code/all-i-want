/**
 * Navigation Bar
 * @module nav-bar
 */

define(function (require) {
    'use strict';

    var $ = require('jquery');
    var navTmpl = require('js/templates/page-nav.tmpl');

    /**
     * @class
     * @exports nav-bar
     * @param {User} user - the currently logged-in user
     */
    function NavBar(user) {
        this.user = user;
    }

    /** Draw the nav-bar */
    NavBar.prototype.render = function () {

        var data = {
            path: window.location.pathname,
            nickname: this.user.nickname,
            logout: {
                url: this.user.logoutUrl,
                label: 'Logout',
            },
            links: [{
                url: '/',
                name: 'Lists',
            }, {
                url: '/groups',
                name: 'Groups',
            }],
            userLinks: [{
                url: '/settings',
                name: 'Settings',
            }],
        };

        if (this.user.isAdmin) {
            data.userLinks.push({
                url: '/requests',
                name: 'Requests',
            });
        }

        $('body').prepend(navTmpl(data));
    };

    return NavBar;
});
