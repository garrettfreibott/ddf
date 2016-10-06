import React from 'react'

import { connect } from 'react-redux'

import styles from './errors.less'

const Errors = ({ errors }) => (
  <div className={styles.message}>
    {errors}
  </div>
)

const mapStateToProps = ({ errors }) => ({ errors })

export default connect(mapStateToProps)(Errors)
