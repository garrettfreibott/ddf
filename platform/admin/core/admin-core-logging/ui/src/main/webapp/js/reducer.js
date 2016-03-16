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

module.exports = function (state, action) {
  if (state === undefined) {
    return {
      logs: [],
      filter: { level: 'ALL' },
      open: true
    }
  }

  switch (action.type) {
    case 'FILTER_LOGS':
      return m(true, state, {
        filter: m(true, state.filter, action.filter)
      })

    case 'APPEND_LOGS':
      return m(true, state, {
        logs: [action.entry].concat(state.logs)
      })

    case 'TOGGLE_LOGS':
      return m(true, state, {
        open: !state.open
      })

    default:
      return state
  }
}
