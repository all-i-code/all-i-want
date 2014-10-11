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

// --------------------------------------------------------
// PATHS --------------------------------------------------
// --------------------------------------------------------
var paths = {};
paths.bower = ['bower_components/**'];
paths.libs = ['libs/**'];
paths.css = ['src/**/*.css'];
paths.js = {};
paths.js.src = ['src/js/**/*.js'];
paths.js.all = paths.js.src.concat([
    './gulpfile.js',
    'spec/js/**/*.js',
]);
paths.less = ['src/**/*.less'];
paths.jade = ['src/templates/**/*.jade'];

// --------------------------------------------------------
// TASKS --------------------------------------------------
// --------------------------------------------------------
gulp.task('lint', function () {
    gulp.src(paths.js.all)
        .pipe(eslint({rulesdir: 'eslint-rules'}))
        .pipe(eslint.format());
}); // lint //

gulp.task('jade', function () {
    gulp.src(paths.jade)
        .pipe(jade({client: true}))
        .pipe(wrap({deps: ['jade'], params: ['jade']}))
        .pipe(rename({extname: '.tmpl.js'}))
        .pipe(gulp.dest('build/js/templates/'));
}); // jade //

gulp.task('libs', function () {
    gulp.src(paths.bower)
        .pipe(gulp.dest('build/bower_components'));

    gulp.src(paths.libs)
        .pipe(gulp.dest('build/libs'));
});

gulp.task('build', ['libs'], function () {
    gulp.src(paths.js.src)
        .pipe(gulp.dest('build/js'));
});

gulp.task('watch', function () {
    gulp.watch(paths.js.all, ['lint']);
    gulp.watch(paths.js.src, ['build']);
    gulp.watch(paths.jade, ['jade']);
});

gulp.task('default', ['lint', 'jade', 'build'], function () {
});
