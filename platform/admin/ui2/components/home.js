import React from 'react'

import { Card, CardActions, CardHeader } from 'material-ui/Card'
import FlatButton from 'material-ui/FlatButton'

import Flexbox from 'flexbox-react'

import Action from '../containers/action'
import Question from '../containers/question'
import Loading from '../containers/loading'
import Errors from '../containers/errors'

const Form = ({ title, questions = [] }) => (
  <div>
    {questions.map((q, i) => <Question key={i} {...q} />)}
  </div>
)

export default ({ stage: { form, actions = [] } }) => (
  <Card style={{ margin: 20, padding: 20, boxSizing: 'border-box' }}>
    <CardHeader
      title={form.title} />

    <CardActions>
      <Loading />

      <Flexbox justifyContent='center'>
        <Form {...form} />
      </Flexbox>

      <Flexbox justifyContent='space-between'>
        <FlatButton label='Back' secondary />
        <div>
          {actions.map((a, i) => <Action key={i} {...a} />)}
        </div>
      </Flexbox>

      <Errors />
    </CardActions>
  </Card>
)
