import React from 'react'

import { back } from '../actions'
import { connect } from 'react-redux'

import { canGoBack, isSubmitting } from '../reducer'

import FlatButton from 'material-ui/FlatButton'

const Back = ({ canGoBack, submitting, label, onBack, ...rest }) => (
  <FlatButton onClick={onBack} label='back' secondary disabled={submitting || !canGoBack} />
)

const mapStateToProps = (state) => ({
  canGoBack: canGoBack(state),
  submitting: isSubmitting(state)
})

export default connect(mapStateToProps, { onBack: back })(Back)
