var test = require('tape')
var batch = require('../../main/webapp/js/batch')

var one = { message: '1', timestamp: 1 }

test('log added to array', function (t) {
  t.timeoutAfter(50)
  t.plan(2)

  var d = batch(1)

  d.on('data', function (entryList) {
    t.equal(entryList[0].timestamp, one.timestamp)
    t.equal(entryList[0].message, one.message)
  })

  d.write(one)
})
