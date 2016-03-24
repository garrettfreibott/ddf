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
// note: user will not lose context when scrolling
exports.append = function (entries) {
  return {
    type: 'APPEND_LOGS',
    entries: entries
  }
}

// for entirely replacing the stored log list
// note: user will lose context when scrolling
exports.set = function (logs) {
  return {
    type: 'SET_LOGS',
    logs: logs
  }
}

// used for expanding the max amount of stored logs displayed on screen
exports.grow = function () {
  return {
    type: 'GROW_LOGS'
  }
}
