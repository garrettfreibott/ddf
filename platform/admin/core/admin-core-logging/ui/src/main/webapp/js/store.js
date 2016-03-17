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

var redux = require('redux')

var reducer = require('./reducer')
var actions = require('./actions')
var random = require('./random-entry')
var http = require('http')
var concat = require('concat-stream')

var store = redux.createStore(reducer)

var getLogs = function (done) {
  http.get({
    path : '/jolokia/exec/org.codice.ddf.admin.logging.LoggingServiceBean:service=logging-service/retrieveLogEvents'
  }, function (res) {
    res.pipe(concat(function (body) {
      try {
        done(null, JSON.parse(body))
      } catch (e) {
        done(e)
      }
    }))
  })
}

setInterval(function () {
  store.dispatch(actions.grow())
}, 250)

setInterval(function () {
  getLogs(function (err, body) {
    if (err) {
    } else {
      store.dispatch(actions.append(body.value))
    }
  })
}, 2000)

module.exports = store
