import { connect } from 'react-redux'
import Home from '../components/home'

const mapStateToProps = ({ stage }) => ({ stage })

export default connect(mapStateToProps)(Home)
