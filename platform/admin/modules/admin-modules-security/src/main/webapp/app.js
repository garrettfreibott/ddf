import React from 'react'
import { Provider } from 'react-redux'
import store from './store'

import Stage from './containers/stage'
import StageList from './containers/stage-list'
import Exception from './containers/exceptions'

import { Router, Route, hashHistory } from 'react-router'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import AppBar from 'material-ui/AppBar'
import Flexbox from 'flexbox-react'

const App = (props) => (
  <div>
    <AppBar title='Security UI' iconClassNameRight='muidocs-icon-navigation-expand-more' />

    <Flexbox>
      <Flexbox>
        <StageList />
      </Flexbox>
      <Flexbox>
        {props.children}
      </Flexbox>
    </Flexbox>
     <Exception />
  </div>
)

var DevTools

if (process.env.NODE_ENV === 'production') {
  DevTools = () => null
}

if (process.env.NODE_ENV !== 'production') {
  DevTools = require('./containers/dev-tools').default
}

export default () => (
  <MuiThemeProvider>
    <Provider store={store}>
      <div>
        <Router history={hashHistory}>
          <Route path='/' component={App}>
            <Route path='/stage/:stageId' component={Stage} />
          </Route>
        </Router>
        <DevTools />
      </div>
    </Provider>
  </MuiThemeProvider>
)
