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

export const submit = ({ method, url }) => (dispatch, getState) => {
  dispatch({ type: 'SUBMITTING_START' })
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
      dispatch({ type: 'SUBMITTING_END' })
      dispatch({
        type: 'NETWORK_ERROR',
        message: 'Cannot submit form. Network error.'
      })
    } else {
      res.pipe(concat((body) => {
      }))
      dispatch({ type: 'SUBMITTING_END' })
    }
  })

  req.write(JSON.stringify(state.stage))
  req.end()
}

