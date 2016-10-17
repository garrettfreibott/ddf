import * as api from './api'

import { getCurrentStage } from './reducer'

export const setStage = (stage) => ({ type: 'SET_STAGE', stage })

export const fetch = () => (dispatch) => {
  api.fetch('discover-sources').then(stage => {
    dispatch(setStage(stage))
  })
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

