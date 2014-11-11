
require(['jquery', 'js/settings-view'], function ($, SettingsView) {
    'use strict';
    var view = new SettingsView($('body'));
    view.init();
});
