/*
1. ask the right questions
2. provide some possible answers
3. convery dependency between qustions/answers
4. make it easy to get the right answers
5. do the thing for the user

what about partially setup things?
*/

/*
validation
  id :id

input
  id :id
  is valid :list validation
  render :list


question
  id :id
  what to ask: :string
  brief explanation :string
  possible answers :list :input
  how to answer the question :input
*/

/*
an input looks like: 

NOTE: inputs are tightly coupled to questions, don't go breaking input apis

a question looks like:
{
  id: 0,
  text: 'would you like to setup ldap?',
  help: null, // no help text to display to user
  inputs: [{ // zero or more inputs to help user answer question
    input_id: 'yes/no', // id of input for question
    args: 'none', // any arguments for the input
    valid: null // validation parameters passed to input
  }],
  confirm: false // should the answer be confirmed with the user?
}

an answer looks like
{
  id: 1012,
  question_id: 0,
  outputs: [{
    input_id: 'yes/no',
    value: 'yes' // this value is input dependent
  }]
}

I am noticing that questions are just another form of input, it's just a little recursive.

Maybe a more generic object would be a 'query'? It might be overloaded, but it fits better.

The backend is querying the user for information!!!

*/

import http from 'http'
import concat from 'concat-stream'

const getWizards = (done) => {
  http.get('/admin/wizards', (res) => {
    res.pipe(concat((body) => {
      try {
        done(null, JSON.parse(body))
      } catch (e) {
        done(e)
      }
    }))
  })
}

const setWizards = (wizards) =>  ({
  type: 'SET_WIZARDS',
  wizards
})

export const fetch = () => (dispatch) => {
  getWizards((err, body) => {
    if (err) {
    } else {
      dispatch(setWizards(body))
    }
  })
}

