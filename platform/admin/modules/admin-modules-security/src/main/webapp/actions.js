import * as api from './api'

import { getCurrentStage } from './reducer'

export const setStage = (stage) => ({ type: 'SET_STAGE', stage })
export const resetStage = (stage) => ({ type: 'RESET_STAGE', stage })

export const fetch = (stageId) => (dispatch) => {
  api.fetchStage(stageId).then(stage => {
    dispatch(resetStage(stage))
  })
}

export const setList = (list) => ({ type: 'SET_LIST', list })

export const list = () => (dispatch) => {
  api.list()
    .then((list) => dispatch(setList(list)))
}

export const edit = (id, value) => ({ type: 'EDIT_VALUE', id, value })
export const submittingStart = () => ({ type: 'SUBMITTING_START' })
export const submittingEnd = () => ({ type: 'SUBMITTING_END' })
export const back = () => ({ type: 'BACK_STAGE' })
export const changeDisplay = (value) => ({ type: 'CHANGE_DISPLAY_TYPE', value })

export const networkError = () => ({
  type: 'ERROR',
  message: 'Cannot submit form. Network error.'
})

export const dismissErrors = () => ({
  type: 'DISMISS_ERRORS'
})

export const submit = (action) => (dispatch, getState) => {
  const stage = getCurrentStage(getState())

  dispatch(submittingStart())

  api.submit(stage, action)
    .then(stage => {
      dispatch(submittingEnd())
      dispatch(setStage(stage))
    }, () => {
      dispatch(submittingEnd())
      dispatch(networkError())
    })
}

