import { connect } from 'react-redux'
import Home from '../components/home'

const mapStateToProps = ({ wizards }) => ({ wizards })

export default connect(mapStateToProps)(Home)
