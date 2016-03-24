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

// adds back-pressure to handle high-volume logs (like TRACE)
module.exports = function (interval) {
  var that
  var buff = []
  var emit = function (data) {
    that.emit('data', data)
  }

  var flush = function () {
    if (that !== undefined && buff.length > 0) {
      emit(buff)
      buff = []
    }
  }

  var i = setInterval(flush, interval || 250)

  return es.through(function (data) {
    that = this
    buff.unshift(data)
  }, function () {
    clearInterval(i)
    flush()
    this.emit('end')
  })
}
