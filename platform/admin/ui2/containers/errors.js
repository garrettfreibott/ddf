import React from 'react'

import { connect } from 'react-redux'
import { getErrors } from '../reducer'

import { dismissErrors } from '../actions'

import Flexbox from 'flexbox-react'

import styles from './errors.less'

const Errors = ({ errors, onDismiss }) => {
  if (errors === null) return null

  return (
    <div className={styles.message}>
      <Flexbox justifyContent='space-between'>
        {errors}
        <div className={styles.dismiss} onClick={onDismiss}>
          <i className='fa fa-times' />
        </div>
      </Flexbox>
    </div>
  )
}

const mapStateToProps = (state) => ({ errors: getErrors(state) })

export default connect(mapStateToProps, { onDismiss: dismissErrors })(Errors)
