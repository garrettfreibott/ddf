import React from 'react'

import { connect } from 'react-redux'
import Home from '../components/home'

import { getAllStages } from '../reducer'

const AllStages = ({ stages = [] }) => <div>{stages.map((stage, i) => <Home key={i} {...stage} />).reverse()}</div>

const mapStateToProps = (state) => ({ stages: getAllStages(state) })

export default connect(mapStateToProps)(AllStages)
