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
var VisibilitySensor = require('react-visibility-sensor')

var LevelSelector = require('./level-selector')
var TextFilter = require('./text-filter')
var LogEntry = require('./log-entry')
var actions = require('./actions')

var filter = require('./filter')

var styles = function () {
  var border = '#ccc'
  var fg = '#2c3e50'
  var bg = '#fff'

  return {
    container: {
      display: 'table',
      height: '100%',
      width: '100%',
      background: '#ccc'
    },
    table: {
      color: fg,
      background: bg,
      borderCollapse: 'collapse',
      width: '100%'
    },
    header: {
      color: fg,
      background: '#ecf0f1',
      padding: 8
    },
    scroll: {
      display: 'block',
      overflowY: 'auto',
      overflowX: 'hidden',
      height: '100%'
    },
    bar: {
      display: 'table-row',
      height: 64,
      boxShadow: '0 0 2px black'
    },
    logs: {
      display: 'table-row',
      position: 'relative'
    },
    controls: {
      padding: 4,
      borderBottom: '1px ' + border + ' solid'
    }
  }
}

var select = function (dispatch) {
  return function (level) {
    dispatch(actions.filter({ level: level }))
  }
}

var textFilter = function (field, props) {
  var on = function (o) {
    props.dispatch(actions.filter(o))
  }

  return (
    <TextFilter field={field} value={props.filter[field]} onChange={on} />
  )
}

var scroll = function (dispatch) {
  return function (isVisible) {
    if (isVisible) {
      dispatch(actions.grow())
    }
  }
}

var LogViewer = function (props) {
  var s = styles()

  var filteredLogs = filter(props.filter, props.logs)

  var displayedLogs = filteredLogs.slice(0, props.displaySize)
    .map(function (entry) {
      return <LogEntry entry={entry} />
    })

  var loading = function () {
    if (filteredLogs.length > 0 && displayedLogs.length < filteredLogs.length) {
      return (
        <VisibilitySensor onChange={scroll(props.dispatch)}>
          <span>Loading...</span>
        </VisibilitySensor>
      )
    }
  }

  return (
    <div style={s.container}>
      <div style={s.bar}>
        <table style={s.table}>
          <thead>
            <tr>
              <td style={s.header} width={250}>
                Time
              </td>
              <td style={s.header} width={100}>
                Level
              </td>
              <td style={s.header}>
                Message
              </td>
              <td style={s.header} width={200}>
                App
              </td>
              <td style={s.header} width={200}>
                Bundle
              </td>
            </tr>
            <tr>
              <td style={s.controls}></td>
              <td style={s.controls}>
                <LevelSelector selected={props.filter.level} onSelect={select(props.dispatch)} />
              </td>
              <td style={s.controls}>
                {textFilter('message', props)}
              </td>
              <td style={s.controls}>
                {textFilter('app', props)}
              </td>
              <td style={s.controls}>
                {textFilter('bundleName', props)}
              </td>
            </tr>
          </thead>
        </table>
      </div>
      <div style={{ display: 'table-row', position: 'relative' }}>
        <div style={s.scroll}>
          <table style={s.table}>
            <tbody>
              {displayedLogs}
            </tbody>
          </table>
          {loading()}
        </div>
      </div>
    </div>
  )
}

module.exports = LogViewer