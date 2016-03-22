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

var es = require('event-stream')
var redux = require('redux')

var backend = require('./backend')
var actions = require('./actions')
var uniq = require('./uniq')
var reducer = require('./reducer')
var store = redux.createStore(reducer)

var batch = function () {
  var emit
  var buff = []

  setInterval(function () {
    if (emit && buff.length > 0) {
      emit(buff)
      buff = []
    }
  }, 250)

  return es.through(function (data) {
    buff.unshift(data)

    emit = function (data) {
      this.emit('data', data)
    }.bind(this)
  })
}

backend()
  .pipe(es.map(function (data, done) {
    setTimeout(function () {
      done(null, data)
    }, 1000)
  }))
  .pipe(uniq())
  .pipe(batch())
  .on('data', function (data) {
    store.dispatch(actions.append(data))
  })

module.exports = store
