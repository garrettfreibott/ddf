var test = require('tape')
var filter = require('../../main/webapp/js/filter')
var random = require('../../main/webapp/js/random-entry')

test('level filter', function (t) {
  t.plan(1)

  var logs = [{ level: 'DEBUG' }, { level: 'WARN' }].map(random)
  var filtered = filter({ level: 'DEBUG' }, logs)

  t.equal(filtered.length, 1)
})

test('text filter', function (t) {
  t.plan(1)

  var logs = [{ message: 'first' }, { message: 'second' }].map(random)
  var filtered = filter({ message: 'first' }, logs)

  t.equal(filtered.length, 1)
})

test('text filter (partial match)', function (t) {
  t.plan(1)

  var logs = [{ message: 'random' }].map(random)
  var filtered = filter({ message: 'rand' }, logs)

  t.equal(filtered.length, 1)
})

test('text filter (regex match)', function (t) {
  t.plan(1)

  var logs = [{ message: 'one two' }, { message: 'two one' }].map(random)
  var filtered = filter({ message: '^one' }, logs)

  t.equal(filtered.length, 1)
})

test('compound filter (logical AND)', function (t) {
  t.plan(1)

  var logs = [
    { level: 'DEBUG', message: 'first' },
    { level: 'WARN', message: 'second' }
  ].map(random)

  var filtered = filter({ level: 'DEBUG', message: 'second' }, logs)

  t.equal(filtered.length, 0)
})
