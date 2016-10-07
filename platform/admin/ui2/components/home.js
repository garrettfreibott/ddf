import React from 'react'

import { Card, CardActions, CardHeader } from 'material-ui/Card'

import Flexbox from 'flexbox-react'

import Action from '../containers/action'
import Question from '../containers/question'
import Loading from '../containers/loading'
import Errors from '../containers/errors'
import Back from '../containers/back'

const Form = ({ title, questions = [] }) => (
  <div>
    {questions.map((q, i) => <Question key={i} {...q} id={i} />)}
  </div>
)

export default ({ form = { title: 'Loading...' }, actions = [] }) => (
  <Card style={{ margin: 20, padding: 20, boxSizing: 'border-box' }}>
    <CardHeader
      title={form.title} />

    <CardActions>
      <Loading />

      <Flexbox justifyContent='center'>
        <Form {...form} />
      </Flexbox>

      <Flexbox justifyContent='space-between'>
        <Back />
        <div>
          {actions.map((a, i) => <Action key={i} {...a} />)}
        </div>
      </Flexbox>

      <Errors />
    </CardActions>
  </Card>
)
