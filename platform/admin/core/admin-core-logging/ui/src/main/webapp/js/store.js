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
var url = require('url')

var data = require('./data.json')

var store = redux.createStore(reducer)

/*for (var i = 0; i < 50; i++) {
  store.dispatch(actions.append(random()))
}*/

/*setInterval(function () {
  //store.dispatch(actions.append(random()))
}, 1000)*/

var entries = data.value

for (var i = 0; i < 7; i++) {
  entries = entries.concat(entries)
}

console.log(entries.length)

store.dispatch(actions.set(entries))

setInterval(function () {
  store.dispatch(actions.grow())
}, 250)
/*setInterval(function () {
  store.dispatch(actions.set(entries))
}, 3000)*/

module.exports = store
