var m = require('merge')
var random = require('random-item')

var levels = require('../../main/webapp/js/levels')

var bundleName = [
  'catalog.bundle',
  'platform.bundle',
  'security.bundle',
  'utils.bundle'
]

var bundleVersion = [
  '1.2.3',
  '3.4.5',
  '5.6.7'
]

var messages = [
  'First log message',
  'Second log message',
  'Third log message'
]

module.exports = function (o) {
  return m({
    timestamp: (new Date()).toISOString(),
    level: random(levels().slice(1)),
    bundleName: random(bundleName),
    bundleVersion: random(bundleVersion),
    message: random(messages)
  }, o)
}
