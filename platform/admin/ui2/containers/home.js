import React from 'react'

import { connect } from 'react-redux'
import Home from '../components/home'
import Errors from '../containers/errors'

import { getAllStages } from '../reducer'

const AllStages = ({ stages = [] }) => (
  <div>
    <Errors />
    {stages.map((stage, i) => <Home key={i} {...stage} />).reverse()}
  </div>
)

const mapStateToProps = (state) => ({ stages: getAllStages(state) })

export default connect(mapStateToProps)(AllStages)
