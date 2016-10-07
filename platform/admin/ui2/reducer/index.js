import { combineReducers } from 'redux-immutable'

import { fromJS } from 'immutable'

const stage = (state = fromJS([{}]), { type, stage, id, value } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return state.unshift(fromJS(stage))
    case 'EDIT_VALUE':
      return state.update(0, s => s.updateIn(['form', 'questions'], qs => qs.update(id, q => q.set('value', value))))
    case 'BACK_STAGE':
      if (state.size > 1) {
        return state.shift()
      } else {
        return state
      }
    default:
      return state
  }
}

export const getCurrentStage = (state) => state.get('stage').first().toJS()

const submitting = (state = false, { type } = {}) => {
  switch (type) {
    case 'SUBMITTING_START':
      return true
    case 'SUBMITTING_END':
      return false
    default:
      return state
  }
}

export const isSubmitting = (state) => state.get('submitting')

const errors = (state = null, { type, message } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return null
    case 'ERROR':
      return message
    default:
      return state
  }
}

export default combineReducers({ errors, submitting, stage })
