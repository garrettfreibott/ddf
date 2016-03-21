var es = require('event-stream')
var concat = require('concat-stream')
var http = require('http')

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
    var that = this

    getLogs(function (err, body) {
      if (err) {
        done(err)
      } else {
        var logs = body.value
        logs.forEach(function (entry) {
          that.emit('data', entry)
        })
        //that.emit('data', logs)

        setTimeout(done, 1000)
      }
    })
  })
}
