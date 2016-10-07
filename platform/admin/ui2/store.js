import { createStore, compose, applyMiddleware } from 'redux'
import thunk from 'redux-thunk'

import { Map } from 'immutable'

import reducer from './reducer'
import DevTools from './containers/dev-tools'

const enhancer = compose(applyMiddleware(thunk), DevTools.instrument())

const store = createStore(reducer, Map(), enhancer)

if (module.hot) {
  module.hot.accept('./reducer', () => {
    const nextReducer = require('./reducer').default
    store.replaceReducer(nextReducer)
  })
}

export default store
