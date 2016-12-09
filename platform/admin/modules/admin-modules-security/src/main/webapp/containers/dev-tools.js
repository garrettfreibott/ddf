import React from 'react'

import { createDevTools } from 'redux-devtools'

import LogMonitor from 'redux-devtools-log-monitor'
import DockMonitor from 'redux-devtools-dock-monitor'
import Dispatcher from 'redux-devtools-dispatch'
import MultipleMonitors from 'redux-devtools-multiple-monitors'

import * as actions from '../actions'

const DevTools = createDevTools(
  <DockMonitor
    toggleVisibilityKey='ctrl-h'
    changePositionKey='ctrl-q'
    defaultSize={0.35}
    defaultPosition='right'
    defaultIsVisible={false}>
    <MultipleMonitors>
      <LogMonitor
        theme='solarized'
        expandActionRoot={false}
        select={(state) => state.toJS()} />
      <Dispatcher
        theme='solarized'
        actionCreators={actions} />
    </MultipleMonitors>
  </DockMonitor>
)

export default DevTools