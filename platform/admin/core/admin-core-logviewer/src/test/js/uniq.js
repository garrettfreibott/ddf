var test = require('tape')
var uniq = require('../../main/webapp/js/uniq')
var random = require('./random-entry')

var one = { message: '1', timestamp: 1 }
var two = { message: '2', timestamp: 2 }

test('old entry (duplicate)', function (t) {
  t.timeoutAfter(25)
  t.plan(2)

  var d = uniq()

  d.on('data', function (entry) {
    t.equal(entry.message, one.message)
    t.equal(entry.timestamp, one.timestamp)
  })

  d.write(one)
  d.write(one)
})

test('new entry', function (t) {
  t.timeoutAfter(25)
  t.plan(4)

  var d = uniq()
  var count = 0, seq = [one, two]

  d.on('data', function (entry) {
    t.equal(entry.message, seq[count].message)
    t.equal(entry.timestamp, seq[count++].timestamp)
  })

  d.write(one)
  d.write(two)
})

test('same timestamp, different entry', function (t) {
  t.timeoutAfter(25)
  t.plan(4)

  var d = uniq()
  var onePointFive = { message: '.5', timestamp: 1 }

  var count = 0, seq = [one, onePointFive]
  d.on('data', function (entry) {
    t.equal(entry.message, seq[count].message)
    t.equal(entry.timestamp, seq[count++].timestamp)
  })

  d.write(one)
  d.write(onePointFive)
})