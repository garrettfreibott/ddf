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

var dom = require('react-dom')
var React = require('react')
var es = require('event-stream')

var store = require('./store')
var LogPanel = require('./log-panel')
var backend = require('./backend')
var actions = require('./actions')

var render = function (data) {
    dom.render(
      <LogPanel state={store.getState()} dispatch={store.dispatch} />,
      document.getElementById('root'))
}

backend()
.pipe(es.map(function (data, done) {
  setTimeout(function () {
    done(null, data)
  }, 0)
}))
.on('data', function (data) {
store.dispatch(actions.append(data))
})

setInterval(function () {
  store.dispatch(actions.grow())
}, 500)

render()
store.subscribe(render)
