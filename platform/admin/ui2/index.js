import 'font-awesome-webpack'
import inject from 'react-tap-event-plugin'

import React from 'react'
import { render } from 'react-dom'
import { AppContainer } from 'react-hot-loader'
import App from './app'

inject()

render(
  <AppContainer errorReporter={({ error }) => { throw error }}>
    <App />
  </AppContainer>,
  document.getElementById('root'))

if (module.hot) {
  module.hot.accept('./app', () => {
    // If you use Webpack 2 in ES modules mode, you can
    // use <App /> here rather than require() a <NextApp />.
    try {
      const NextApp = require('./app').default
      render(
        <AppContainer errorReporter={({ error }) => { throw error }}>
          <NextApp />
        </AppContainer>,
        document.getElementById('root'))
    } catch (e) {}
  })
}
