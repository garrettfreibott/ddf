import React from 'react'

import { submit } from '../actions'
import { connect } from 'react-redux'

import RaisedButton from 'material-ui/RaisedButton'

const Action = ({ submitting, label, onSubmit, ...rest }) => (
  <div>
    <RaisedButton onClick={() => { onSubmit(rest) }} label={label} primary disabled={submitting} />
  </div>
)

const mapStateToProps = ({ submitting }) => ({ submitting })

export default connect(mapStateToProps, { onSubmit: submit })(Action)
