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
var moment = require('moment')

var levels = require('./levels')

var format = function (time) {
  return moment(time).format('MMMM Do YYYY, h:mm:ss a')
}

var styles = function (level) {
  return {
    borderBottom: '1px #ccc solid',
    padding: 5,
    background: levels(level)
  }
}

var LogEntry = function (props) {
  var entry = props.entry
  var s = styles(entry.level)

  return (
    <tr>
      <td style={s} width={250}>
        {format(entry.time)}
      </td>
      <td style={s} width={100}>
        {entry.level}
      </td>
      <td style={s}>
        {entry.message}
      </td>
      <td style={s} width={200}>
        {entry.app}
      </td>
      <td style={s} width={200}>
        {entry.bundle}
      </td>
    </tr>
  )
}

module.exports = LogEntry
