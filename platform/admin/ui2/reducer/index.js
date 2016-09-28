import { combineReducers } from 'redux'

const wizards = (state = [], { type, wizards }) => {
  switch (type) {
    case 'SET_WIZARDS':
      return wizards
    default:
      return state
  }
}

export default combineReducers({wizards})
