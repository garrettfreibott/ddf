// @flow

import React from 'react'
import { Provider } from 'react-redux'
import store from './store'
import DevTools from './containers/dev-tools'
import Home from './containers/home'

import { Router, Route, browserHistory } from 'react-router'
import { fetch } from './actions'

store.dispatch(fetch())

export default () => (
  <Provider store={store}>
    <div>
      <Router history={browserHistory}>
        <Route path='/' component={Home} />
      </Router>
      <DevTools />
    </div>
  </Provider>
)
