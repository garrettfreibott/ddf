import { connect } from 'react-redux'
import Home from '../components/home'

import { getCurrentStage } from '../reducer'

const mapStateToProps = (state) => getCurrentStage(state)

export default connect(mapStateToProps)(Home)
