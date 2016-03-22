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
