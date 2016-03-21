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

var m = require('merge')

// total number of log entries to keep in memory that a
// user can filter through
var MAX_FILTER_ENTRIES = 10000

// total number of logs that can be displayed for the user
var MAX_INITIAL_DISPLAY = 50

module.exports = function (state, action) {
  if (state === undefined) {
    return {
      logs: [],
      filter: { level: 'ALL' },
      open: true,
      displaySize: 10 // number of rows to display
    }
  }

  switch (action.type) {
    case 'FILTER_LOGS':
      return m(true, state, {
        displaySize: 10,
        filter: m(true, state.filter, action.filter)
      })

    case 'APPEND_LOGS':
      return m(true, state, {
        logs: action.entries.concat(state.logs).slice(0, MAX_FILTER_ENTRIES)
      })

    case 'TOGGLE_LOGS':
      return m(true, state, {
        open: !state.open
      })

    case 'SET_LOGS':
      return m(true, state, {
        logs: action.logs
      })

    case 'GROW_LOGS':
      return m(true, state, {
        displaySize: state.displaySize + 10
      })

    default:
      return state
  }
}
