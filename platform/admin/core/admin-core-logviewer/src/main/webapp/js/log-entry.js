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
  return moment(time).format('D MMM YYYY, HH:mm:ss')
}

var styles = function (level) {
  return {
    row: {
      borderBottom: '1px #ccc solid',
      padding: 5,
      background: levels(level),
      wordWrap: 'break-word'
    },
    dim: {
      opacity: 0.5
    }
  }
}

// log entry to display
var LogEntry = function (props) {
  var entry = props.entry
  var marks = props.marks
  var s = styles(entry.level)

  // check if marks exist for filter highlighting
  var tryMark = function (key) {
    var mark = marks[key]
    var displayString = entry[key]
    if (mark) {
      var first = displayString.slice(0, mark.start)
      var second = displayString.slice(mark.start, mark.end)
      var third = displayString.slice(mark.end)
      return (
        <span>
          <span style={s.dim}>{first}</span>
          <mark>{second}</mark>
          <span style={s.dim}>{third}</span>
        </span>
      )
    } else {
      return (
        <span>{displayString}</span>
      )
    }
  }

  return (
  <tr>
    <td style={s.row} width={175}>
      {format(entry.timestamp)}
    </td>
    <td style={s.row} width={75}>
      {entry.level}
    </td>
    <td style={s.row}>
      {tryMark('message')}
    </td>
    <td style={s.row} width={200}>
      {tryMark('bundleName')}
    </td>
  </tr>
  )
}

module.exports = LogEntry
