import React from 'react'

import { back } from '../actions'
import { connect } from 'react-redux'

import { isSubmitting } from '../reducer'

import FlatButton from 'material-ui/FlatButton'

const Back = ({ submitting, label, onBack, ...rest }) => (
  <div>
    <FlatButton onClick={onBack} label='back' secondary disabled={submitting} />
  </div>
)

const mapStateToProps = (state) => ({ submitting: isSubmitting(state) })

export default connect(mapStateToProps, { onBack: back })(Back)
