import React from 'react'

import { submit } from '../actions'
import { connect } from 'react-redux'

import { isSubmitting } from '../reducer'

import RaisedButton from 'material-ui/RaisedButton'

const Action = ({ submitting, label, onSubmit, ...rest }) => (
  <div>
    <RaisedButton onClick={() => { onSubmit(rest) }} label={label} primary disabled={submitting} />
  </div>
)

const mapStateToProps = (state) => ({ submitting: isSubmitting(state) })

export default connect(mapStateToProps, { onSubmit: submit })(Action)
