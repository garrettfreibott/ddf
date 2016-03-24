var test = require('tape')
var reducer = require('../../main/webapp/js/reducers/reducer')
var actions = require('../../main/webapp/js/actions/actions')
var random = require('./random-entry')

test('initial state', function (t) {
  t.plan(3)

  var state = reducer()
  t.equal(state.open, true)
  t.equal(state.logs.length, 0)
  t.deepEqual(state.filter, { level: 'ALL' })
})

test('filter logs', function (t) {
  t.plan(1)

  var state = reducer(reducer(), actions.filter({ level: 'DEBUG' }))
  t.equal(state.filter.level, 'DEBUG')
})

test('append logs', function (t) {
  t.plan(1)

  var state = reducer(reducer(), actions.append([random()]))
  t.equal(state.logs.length, 1)
})
