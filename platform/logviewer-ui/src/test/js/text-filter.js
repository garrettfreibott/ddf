var test = require('tape')
var React = require('react')
var dom = require('react-dom')
var utils = require('react-addons-test-utils')
var wrap = require('react-stateless-wrapper').wrap

// need to wrap stateless components to test events
var TextFilter = wrap(require('../../main/webapp/js/text-filter'))

var render = function (component) {
  var c = utils.renderIntoDocument(component)
  return dom.findDOMNode(c)
}

test('on filter text change', function (t) {
  t.plan(1)

  var assert = function (filter) {
    t.deepEqual(filter, { message: 'second' })
  }

  var node = render(<TextFilter value='first' field='message' onChange={assert} />)
  utils.Simulate.change(node, { target: { value: 'second' } })
})
