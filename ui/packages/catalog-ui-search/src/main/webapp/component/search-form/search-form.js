/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
 /*global require*/
 var _ = require('underscore');
 var Backbone = require('backbone');

 module.exports = Backbone.Model.extend({
    defaults: {
        name: "A Search Form",
        description: "",
        createdBy: "admin",
        owner: "admin",
        createdOn: "",
        type: "custom",
        id: undefined,
        filterTemplate: "{\"property\":\"anyText\",\"value\":\"\",\"type\":\"ILIKE\"}",
        descriptors: [],
        accessIndividuals: [],
        accessGroups: [],
        querySettings: {}
    }
 });