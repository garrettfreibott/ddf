import React from 'react'

import { Card, CardActions, CardHeader } from 'material-ui/Card'

import Flexbox from 'flexbox-react'

import Action from '../containers/action'
import Component from '../containers/component'
import Loading from '../containers/loading'
import Back from '../containers/back'

export default ({ form = {}, actions = [] }) => (
  <Card style={{ maxWidth: 600, margin: 20, padding: 20, boxSizing: 'border-box' }}>
    <Loading />
    <Component {...form} />
    <Flexbox style={{marginTop: 20}} justifyContent='space-between'>
      <Back />

      <div>{actions.map((a, i) => <Action key={i} {...a} />)}</div>
    </Flexbox>
  </Card>
)
