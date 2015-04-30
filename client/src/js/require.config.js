/*
 * RequireJS config for all-i-want
 */

/* global requirejs */

(function () {
    'use strict';

    function bower(path) {
        return 'bower_components/' + path;
    }

    requirejs.config({
        baseUrl: '/static',
        packages: [{
            name: 'css',
            location: bower('require-css'),
            main: 'css.min',
        }],
        paths: {
            'jade': bower('jade/runtime'),
            'lodash': bower('lodash/dist/lodash.min'),
            'jquery': bower('jquery/dist/jquery.min'),
            'jquery.bootstrap': bower('bootstrap/dist/js/bootstrap.min'),
            'q': bower('q/q'),
            'q-xhr': bower('q-xhr/q-xhr'),
        },
        deps: [
            'jquery.bootstrap',
        ],
        shim: {
            'jquery.bootstrap': {
                deps: ['jquery'],
            },
        },
    });
}());
