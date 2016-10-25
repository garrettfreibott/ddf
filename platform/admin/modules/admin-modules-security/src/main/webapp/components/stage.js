import React from 'react'

import { Card } from 'material-ui/Card'

import Component from '../containers/component'
import Loading from '../containers/loading'
import Back from '../containers/back'

export default ({ disabled, rootComponent = {}, actions = [], path = [] }) => (
  <Card style={{ width: 600, margin: 20, padding: 20, boxSizing: 'border-box' }}>
    <Loading />
    <Component path={[ ...path, 'rootComponent' ]} {...rootComponent} disabled={disabled} />
    <Back disabled={disabled}/>
  </Card>
)
