var es = require('event-stream')
var concat = require('concat-stream')
var http = require('http')

var logs = []

// retrieves logs from the endpoint
var getLogs = function (done) {
  endpoint = '/jolokia/exec/org.codice.ddf.admin.logging.LoggingServiceBean:service=logging-service/retrieveLogEvents'

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
        }, 50)
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
