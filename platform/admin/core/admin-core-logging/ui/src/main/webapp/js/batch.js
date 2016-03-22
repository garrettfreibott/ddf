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
