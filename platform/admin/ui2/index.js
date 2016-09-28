import 'font-awesome-webpack'

import React from 'react'
import { render } from 'react-dom'
import { AppContainer } from 'react-hot-loader'
import App from './app'

render(
  <AppContainer>
    <App />
  </AppContainer>,
  document.getElementById('root'))

if (module.hot)
  module.hot.accept()

if (module.hot) {
  module.hot.accept('./app', () => {
    // If you use Webpack 2 in ES modules mode, you can
    // use <App /> here rather than require() a <NextApp />.
    const NextApp = require('./app').default
    render(
      <AppContainer>
        <NextApp />
      </AppContainer>,
      document.getElementById('root'))
  })
}
