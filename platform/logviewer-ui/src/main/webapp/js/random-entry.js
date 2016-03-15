
// !!! REMOVE THIS - for testing purposes

var m = require('merge')
var random = require('random-item')

var levels = require('./levels')

var bundles = [
  'catalog.bundle',
  'platform.bundle',
  'security.bundle',
  'utils.bundle'
]

var apps = [
  'Catalog',
  'Platform',
  'Security'
]

var messages = [
  'First log message',
  'Second log message',
  'Third log message'
]

module.exports = function (o) {
  return m({
    time: (new Date()).toISOString(),
    level: random(levels().slice(1)),
    bundle: random(bundles),
    app: random(apps),
    message: random(messages)
  }, o)
}
