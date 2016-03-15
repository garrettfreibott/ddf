/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
/*global define*/
define([
    'backbone',
    'marionette',
    'js/views/module/ModuleDetail.layout',
    'js/models/module/ModulePlugin',
    'js/models/AppConfigPlugin',
    'js/views/application/application-detail/PluginTabContent.view',
    'js/views/application/application-detail/PluginTab.view',
    'q'
],function (Backbone, Marionette, ModuleDetailLayout, ModulePlugin, AppConfigPlugin, PluginTabContentView, PluginTabView, Q) {

    var ModuleDetailController = Marionette.Controller.extend({

        initialize: function(options){
            this.regions = options.regions;
            this.show();
        },
        show: function() {
            var layoutView = new ModuleDetailLayout();
            this.regions.applications.show(layoutView);

            this.fetchSystemConfigPlugins().then(function(systemConfigPlugins){
                var staticModulePlugins = [
                     new Backbone.Model({
                        'id': 'systemInformationModuleTabID',
                        'displayName': 'Information',
                        'javascriptLocation': 'js/views/module/plugins/systeminformation/Plugin.view.js'
                    }),
                    new Backbone.Model({
                        'id': 'featureModuleTabID',
                        'displayName': 'Features',
                        'javascriptLocation': 'js/views/module/plugins/feature/Plugin.view.js'
                    }),
                    new Backbone.Model({
                        'id': 'configurationModuleTabID',
                        'displayName': 'Configuration',
                        'javascriptLocation': 'js/views/module/plugins/configuration/Plugin.view.js'
                     })
                ];

                var staticList = new ModulePlugin.Collection();
                staticList.comparator = function(model) {
                    return model.get('displayName');
                };
                staticList.add(staticModulePlugins);
                staticList.sort();

                var dynamicList = new Backbone.Collection();
                dynamicList.comparator = function(model) {
                    return model.get('displayName');
                };
                dynamicList.add(systemConfigPlugins.models);
                dynamicList.sort();

                var completeList = new Backbone.Collection();
                completeList.add(staticList.models);
                completeList.add(dynamicList.models);

                layoutView.tabs.show(new PluginTabView({collection: completeList}));
                layoutView.tabContent.show(new PluginTabContentView({collection: completeList}));
                layoutView.selectFirstTab();
            }).fail(function(error){
                    throw error;
            });
        },
        fetchSystemConfigPlugins: function(){
            var pageName = 'system-page';
            var collection = new AppConfigPlugin.Collection();
            var defer = Q.defer();
            collection.fetchByAppName(pageName, {
                success: function(){
                    defer.resolve(collection);
                },
                failure: function(){
                    defer.reject(new Error("Error fetching system page plugins for {0}"));
                }
            });
            return defer.promise;
        }
    });

    return ModuleDetailController;



});