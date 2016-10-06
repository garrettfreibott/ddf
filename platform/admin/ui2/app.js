// @flow

import React from 'react'
import { Provider } from 'react-redux'
import store from './store'
import DevTools from './containers/dev-tools'
import Home from './containers/home'

import { Router, Route, browserHistory } from 'react-router'
import { fetch } from './actions'

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'

if (!window.init) {
  store.dispatch(fetch())
  window.init = true
}

export default () => (
  <MuiThemeProvider>
    <Provider store={store}>
      <div>
        <Router history={browserHistory}>
          <Route path='/' component={Home} />
        </Router>
        <DevTools />
      </div>
    </Provider>
  </MuiThemeProvider>
)
