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

var LogViewer = require('./log-viewer')
var actions = require('./actions')

var styles = function (props) {
  return {
    btn: {
      padding: '10px 15px',
      color: '#fff',
      background: '#777',
      border: 0,
      marginRight: 10,
      position: 'absolute',
      right: 0,
      top: -40,
      borderTopLeftRadius: 3,
      borderTopRightRadius: 3,
      fontSize: 14,
      cursor: 'pointer'
    },
    icon: {
      border: '1px solid white',
      borderRadius: '100%',
      display: 'inline-block',
      width: 18,
      height: 18,
      marginLeft: 12
    },
    panel: {
      position: 'fixed',
      //height: (props.state.open) ? '90%' : 0,
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      fontFamily: 'sans-serif',
      fontSize: 14
    }
  }
}

var LogPanel = function (props) {
  var state = props.state
  var dispatch = props.dispatch
  var s = styles(props)

  var toggle = function () {
    dispatch(actions.toggle())
  }

  var viewer = function () {
    if (state.open) {
      return (
        <LogViewer
          filter={state.filter}
          logs={state.logs}
          displaySize={state.displaySize}
          dispatch={dispatch} />
      )
    }
  }

  var icon = function () {
    if (state.open) {
      return <span style={s.icon}>-</span>
    }
    return <span style={s.icon}>+</span>
  }

  var btn = function () {
    return (
      <button style={s.btn} onClick={toggle}>
        Logs
        {icon()}
      </button>
    )
  }

  return (
    <div style={s.panel}>
      {viewer()}
    </div>
  )
}

module.exports = LogPanel
