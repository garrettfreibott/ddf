import React from 'react'

import { Card } from 'material-ui/Card'

import Component from '../containers/component'
import Loading from '../containers/loading'
import Back from '../containers/back'

import CheckCircle from 'material-ui/svg-icons/action/check-circle'
import { green500 } from 'material-ui/styles/colors'

const styles = {
  container: {
    position: 'relative'
  },
  success: {
    position: 'absolute',
    right: 0,
    bottom: 0
  },
  check: {
    height: 30,
    width: 30
  }
}

const Success = ({ isSuccessful }) => {
  if (!isSuccessful) return null
  return (
    <div style={styles.container}>
      <div style={styles.success}>
        <CheckCircle style={styles.check} color={green500} />
      </div>
    </div>
  )
}

export default ({ disabled, rootComponent = {}, actions = [], path = [] }) => (
  <Card style={{ width: 600, margin: 20, padding: 20, boxSizing: 'border-box' }}>
    <Loading />
    <Component path={[ ...path, 'rootComponent' ]} {...rootComponent} disabled={disabled} />
    <Back disabled={disabled} />
    <Success isSuccessful={disabled} />
  </Card>
)
