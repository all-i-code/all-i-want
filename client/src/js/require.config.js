/*
 * RequireJS config for all-i-want
 */

/* global requirejs */

requirejs.config({
    baseUrl: '/static',
    paths: {
        'lodash': 'bower_components/lodash/dist/lodash.min',
        'jquery': 'bower_components/jquery/dist/jquery.min',
        'jquery.bootstrap': 'bower_components/bootstrap/dist/js/bootstrap.min',
    },
    map: {
        '*': {
            'css': 'bower_components/require-css/css.min',
        },
    },
    deps: [
        'jquery.bootstrap',
        'css!libs/bootswatch/slate/bootstrap.min',
        //'css!bower_components/bootstrap/dist/css/bootstrap.min',
    ],
    shim: {
        'jquery.bootstrap': {
            deps: ['jquery'],
        },
    },
});
