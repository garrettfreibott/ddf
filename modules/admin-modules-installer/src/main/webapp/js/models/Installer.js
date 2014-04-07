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
define(['backbone', 'underscore'], function (Backbone, _) {

    var Installer = {};

    var _step = function (direction) {
        var changeObj = {};
        changeObj.stepNumber = this.get('stepNumber') + direction;
        if(changeObj.stepNumber < this.get('totalSteps')) {
            changeObj.hasNext = true;
        } else {
            changeObj.stepNumber = this.get('totalSteps');
            changeObj.hasNext = false;
        }

        if(changeObj.stepNumber > 0) {
            changeObj.hasPrevious = true;
        } else {
            changeObj.stepNumber = 0;
            changeObj.hasPrevious = false;
        }

        return changeObj;
    };

    Installer.Model = Backbone.Model.extend({
        defaults: {
            hasNext: true,
            hasPrevious: false,
            totalSteps: 3,
            stepNumber: 0,
            percentComplete: 0,
            busy: false,
            message: '',
            steps: []
        },
        initialize: function() {
            _.bindAll(this);
            this.on('block', this.block);
            this.on('unblock', this.unblock);
        },
        setTotalSteps: function(numOfSteps) {
            var changeObj = {};
            changeObj.steps = [];
            for(var i=0; i < numOfSteps; i++) {
                changeObj.steps.push({percentComplete: 0});
            }
            changeObj.totalSteps = numOfSteps;
            this.set(changeObj);
        },
        nextStep: function(message, percentComplete) {
            var stepNumber = this.get('stepNumber'),
                totalSteps = this.get('totalSteps'),
                changeObj = {};

            if(stepNumber < totalSteps) {
                if(!_.isUndefined(message)) {
                    changeObj.message = message;
                }

                changeObj.steps = this.get('steps');
                if(!_.isUndefined(percentComplete)) {
                    changeObj.steps[stepNumber].percentComplete = percentComplete;
                } else {
                    changeObj.steps[stepNumber].percentComplete = 100;
                }

                changeObj.percentComplete = 0;
                _.each(changeObj.steps, function(step) {
                    changeObj.percentComplete += step.percentComplete / totalSteps;
                });

                changeObj.percentComplete = Math.round(changeObj.percentComplete);

                if(changeObj.percentComplete > 100) {
                    changeObj.percentComplete = 100;
                }

                if(changeObj.steps[stepNumber].percentComplete === 100) {
                    _.extend(changeObj, _step.call(this, 1));
                }

                this.set(changeObj);
            }
        },
        block: function() {
            this.set({ busy: true });
        },
        unblock: function() {
            this.set({ busy: false });
        },
        previousStep: function() {
            this.set(_step.call(this, -1));
        }
    });

    return Installer;

});