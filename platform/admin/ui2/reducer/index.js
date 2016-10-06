import { combineReducers } from 'redux'

const actions = (state = [], { type, stage } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return stage.actions
    default:
      return state
  }
}

const title = (state = 'loading...', { type, stage } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return stage.form.title
    default:
      return state
  }
}

const question = (state = {}, { type, id, value }) => {
  switch (type) {
    case 'EDIT_VALUE':
      if (id === state.id) {
        return { ...state, value: value }
      } else {
        return state
      }
    default:
      return state
  }
}

const questions = (state = [], { type, stage, ...rest } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return stage.form.questions
    case 'EDIT_VALUE':
      return state.map((q) => question(q, { type, ...rest }))
    default:
      return state
  }
}

const form = combineReducers({ title, questions })

const state = (state = {}, { type, stage } = {}) => {
  switch (type) {
    case 'SET_STAGE':
      return stage.state || state
    default:
      return state
  }
}

const stage = combineReducers({ state, form, actions })

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
