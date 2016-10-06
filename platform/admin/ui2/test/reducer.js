import { check, property, gen } from 'testcheck'
import { submittingStart, submittingEnd, networkError } from '../actions'

import reducer from '../reducer'

const isValid = (state, action, nextState) => {
  switch (action.type) {
    case 'SUBMITTING_START':
      return nextState.submitting === true
    case 'SUBMITTING_END':
      return nextState.submitting === false
    case 'ERROR':
      return nextState.errors !== null
  }
  return true
}

const c = check(
  property(
    [gen.array(
      gen.oneOf([
        gen.return(submittingStart()),
        gen.return(submittingEnd()),
        gen.return(networkError())
      ])
    )],
    (actions) => {
      var state = reducer()

      const badState = actions.find((action) => {
        const nextState = reducer(state, action)
        if (isValid(state, action, nextState)) {
          state = nextState
        } else {
          return true
        }
      })

      return badState === undefined
    }
  ),
  { times: 200, seed: 42 }
)

if (!c.result) {
  console.log(JSON.stringify(c, null, 2))
  process.exit(1)
}

