var es = require('event-stream')
var concat = require('concat-stream')
var http = require('http')
var store = require('./store')
var actions = require('./actions')

var getLogs = function (timestamp, done) {
  var endpoint = '/jolokia/exec/org.codice.ddf.admin.logging.LoggingServiceBean:service=logging-service/retrieveLogEvents/' // After/' + timestamp

  if (timestamp === 0) {
    endpoint = '/jolokia/exec/org.codice.ddf.admin.logging.LoggingServiceBean:service=logging-service/retrieveLogEvents'
  }

  http.get({
    path : endpoint
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
  var timestamp = 0
  return es.readable(function (count, done) {
    var that = this

    getLogs(timestamp, function (err, body) {
      if (err) {
        done(err)
      } else {
        var logs = body.value
        if (timestamp === 0) {
          store.dispatch(actions.set(logs))
          timestamp = logs[logs.length-1].timestamp
        } else {
          for (var i = 0; i < logs.length; i++) {
            if (logs[i].timestamp > timestamp) {
              that.emit('data', logs[i])
              timestamp = logs[i].timestamp
            }
          }
        }

        setTimeout(done, 1000)
      }
    })
  })
}
