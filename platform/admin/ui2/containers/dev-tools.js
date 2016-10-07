import React from 'react'

import { createDevTools } from 'redux-devtools'

import LogMonitor from 'redux-devtools-log-monitor'
import DockMonitor from 'redux-devtools-dock-monitor'

const DevTools = createDevTools(
  <DockMonitor
    toggleVisibilityKey='ctrl-h'
    changePositionKey='ctrl-q'
    defaultSize={0.5}
    defaultPosition='bottom'
    defaultIsVisible>
    <LogMonitor
      theme='solarized'
      expandActionRoot={false}
      select={(state) => state.toJS()} />
  </DockMonitor>
)

export default DevTools
