var es = require('event-stream')
var concat = require('concat-stream')
var http = require('http')

var logs = []

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

module.exports = function () {
  return es.readable(function (count, done) {
    if (logs.length > 0) {
      if (count > 500) {
        setTimeout(function () {
          done(null, logs.shift())
        }, 10)
      } else {
        done(null, logs.shift())
      }
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
