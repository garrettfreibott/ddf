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

// for applying a filter to the logs
exports.filter = function (filter) {
  return {
    type: 'FILTER_LOGS',
    filter: filter
  }
}

// for appending new logs to the stored log list
// note: this will not cause the user to lose context
exports.append = function (entries) {
  return {
    type: 'APPEND_LOGS',
    entries: entries
  }
}

// for entirely replacing the stored log list
// note: this will cause the user to lose context
exports.set = function (logs) {
  return {
    type: 'SET_LOGS',
    logs: logs
  }
}

// used for expanding the amount of logs displayed on screen
// from the stored log list
exports.grow = function () {
  return {
    type: 'GROW_LOGS'
  }
}
