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

var es = require('event-stream')

// filters out old/duplicate logs and stream out new/unique logs
module.exports = function () {
  var lastSet = []

  // uses timestamp + message to determine uniqueness
  var isNotInOldList = function (currentEntry) {
    var found = lastSet.find(function (oldEntry) {
      return (currentEntry.timestamp === oldEntry.timestamp && currentEntry.message === oldEntry.message)
    })

    return found === undefined
  }

  return es.through(function (currentEntry) {
    var that = this

    var emit = function (entry) {
      that.emit('data', entry)
    }

    // emit any new entries
    if (isNotInOldList(currentEntry)) {
      emit(currentEntry)
      lastSet.unshift(currentEntry)
      if (lastSet.length > 500) {
        lastSet.pop()
      }
    }
  })
}
