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

var React = require('react')
var levels = require('./levels')

var options = function () {
  return levels().map(function (level) {
    return (
    <option key={level} value={level}>
      {level}
    </option>
    )
  })
}

var select = function (fn) {
  return function (e) { fn(e.target.value) }
}

// log level selector
var LevelSelector = function (props) {
  return (
  <select value={props.selected} onChange={select(props.onSelect)}>
    {options()}
  </select>
  )
}

module.exports = LevelSelector
