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
var concat = require('concat-stream')
var http = require('http')

var logs = []

// retrieves logs from the endpoint
var getLogs = function (done) {
  var endpoint = '/jolokia/exec/org.codice.ddf.logging.platform.LoggingServiceBean:service=logging-service/retrieveLogEvents'

  http.get({
    path: endpoint
  }, function (res) {
    res.pipe(concat(function (body) {
      try {
        done(null, JSON.parse(body))
      } catch (e) {
        done(e)
      }
    }))
  })
}

// polls the endpoint in batches of 500 and streams them out individually
module.exports = function () {
  return es.readable(function (count, done) {
    if (logs.length > 0) {
      if (count > 500) {
        // set polling interval
        setTimeout(function () {
          done(null, logs.shift())
        }, 10)
      } else {
        done(null, logs.shift())
      }

    // fetch new logs if all have been streamed out
    } else {
      getLogs(function (err, body) {
        if (err) {
          done(err)
        } else {
          logs = body.value
          done(null, logs.shift())
        }
      })
    }
  })
}
