import { combineReducers } from 'redux-immutable'

import { List, Map, fromJS } from 'immutable'

export const getCurrentStage = (state) => state.get('stage').first().toJS()

export const canGoBack = (state) => state.get('stage').size > 2

const stage = (state = fromJS([{}]), { type, stage, id, value } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return state.unshift(fromJS(stage))
    case 'EDIT_VALUE':
      return state.update(0, s => s.updateIn(['form', 'questions'], qs => (qs || List()).update(id, q => (q || Map()).set('value', value))))
    case 'BACK_STAGE':
      if (state.size > 2) {
        return state.shift()
      } else {
        return state
      }
    default:
      return state
  }
}

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
