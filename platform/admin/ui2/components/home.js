import React from 'react'

import Divider from 'material-ui/Divider'
import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card'
import FlatButton from 'material-ui/FlatButton'

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
      title={form.title}
      actAsExpander
      showExpandableButton />

    <CardActions>
      <Divider />
      <Loading />
      <Form {...form} />

      <div style={{padding: '20px 0'}}>
        <Divider />
      </div>

      <FlatButton label='Back' secondary />
      <div style={{textAlign: 'right'}}>
      {actions.map((a, i) => <Action key={i} {...a} />)}
      </div>

      <Errors />
    </CardActions>

    <CardText expandable>
      Some useful help text should be placed here.
    </CardText>
  </Card>
)
