import http from 'http'
import concat from 'concat-stream'
import { parse } from 'url'

import stub from './stub'

const getStage = (url, done) => {
  done(null, stub)
}

const setStage = (stage) => ({ type: 'SET_STAGE', stage })

export const fetch = () => (dispatch) => {
  getStage('/', (err, body) => {
    if (err) {
    } else {
      dispatch(setStage(body))
    }
  })
}

export const edit = (id, value) => ({ type: 'EDIT_VALUE', id, value })
export const submittingStart = () => ({ type: 'SUBMITTING_START' })
export const submittingEnd = () => ({ type: 'SUBMITTING_END' })

export const networkError = () => ({
  type: 'ERROR',
  message: 'Cannot submit form. Network error.'
})

export const badJsonResponse = () => ({
  type: 'ERROR',
  message: 'Invalid network reponse.'
})

export const submit = ({ method, url }) => (dispatch, getState) => {
  dispatch(submittingStart())
  const state = getState()

  const opts = {
    ...parse(url),
    method: method,
    headers: {
      'content-type': 'application/json'
    }
  }

  const req = http.request(opts, (res) => {
    if (res.statusCode !== 200) {
      dispatch(submittingEnd())
      dispatch(networkError())
    } else {
      res.pipe(concat((body) => {
        dispatch(submittingEnd())
        try {
          dispatch(setStage(JSON.parse(body)))
        } catch (e) {
          dispatch(badJsonResponse())
        }
      }))
    }
  })

  req.write(JSON.stringify(state.stage))
  req.end()
}

