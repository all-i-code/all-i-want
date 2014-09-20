/**
 * @file gulpfile.js
 * Initial attempt at using gulp for frontend task management
 * @author Adam Meadows <adam.meadows@gmail.com>
 */

/* eslint-disable global-strict */
'use strict';

var gulp = require('gulp');

// plugins
var eslint = require('gulp-eslint');
var jade = require('gulp-jade');
var rename = require('gulp-rename');
var wrap = require('gulp-wrap-amd');

var ALL_JS_FILES = [
    './gulpfile.js',
    'src/js/**/*.js',
    'spec/js/**/*.js',
];

gulp.task('lint', function () {
    gulp.src(ALL_JS_FILES)
        .pipe(eslint({
            rulesdir: 'eslint-rules',
        }))
        .pipe(eslint.format());
});

gulp.task('jade', function () {
    gulp.src('src/templates/**/*.jade')
        .pipe(jade({
            client: true,
        }))
        .pipe(wrap({
            deps: ['jade'],
            params: ['jade'],
        }))
        .pipe(rename({extname: '.tmpl.js'}))
        .pipe(gulp.dest('src/templates'));
});

gulp.task('default', function () {
});
