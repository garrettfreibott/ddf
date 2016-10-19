import React from 'react'

import MenuItem from 'material-ui/MenuItem'
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField'

import AutoComplete from 'material-ui/AutoComplete'

import { CardActions, CardHeader } from 'material-ui/Card'
import Flexbox from 'flexbox-react'

import { edit } from '../actions'
import { connect } from 'react-redux'

import { isSubmitting } from '../reducer'

var Component

const PortInput = ({ submitting, path, value, error, defaults = [], onEdit }) => (
  <AutoComplete
    dataSource={defaults.map((value) => ({ text: String(value), value: value }))}
    openOnFocus
    filter={AutoComplete.noFilter}
    type='number'
    errorText={error}
    disabled={submitting}
    floatingLabelText='Port'
    searchText={String(value || defaults[0])}
    onNewRequest={({ value }) => { onEdit(path, value) }}
    onUpdateInput={(value) => { onEdit(path, Number(value)) }} />
)

const StringEnumInput = ({ submitting, path, value, label, error, defaults = [], onEdit }) => (
  <SelectField
    value={value || defaults[0] || ''}
    disabled={submitting}
    errorText={error}
    floatingLabelText={label}
    onChange={(e, i, v) => onEdit(path, v)}>
    {defaults.map((d, i) => <MenuItem key={i} value={d} primaryText={d} />)}
  </SelectField>
)

const HostNameInput = ({ submitting, path, value, error, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText='Hostname'
    onChange={(e) => onEdit(path, e.target.value)} />
)

const PasswordInput = ({ submitting, path, label, error, value, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText={label}
    type='password'
    onChange={(e) => onEdit(path, e.target.value)} />
)

const StringInput = ({ submitting, path, label, error, value, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText={label}
    onChange={(e) => onEdit(path, e.target.value)} />
)

const Info = ({id, label, value}) => (
  <div>
    <h3>{label}</h3>
    <p>{value}</p>
  </div>
)

const Panel = ({ id, path = [], label, description, children = [] }) => (
  <div>
    <CardHeader title={label} subtitle={description} />
    <CardActions>
      <Flexbox justifyContent='center'>
        <div>{children.map((c, i) =>
          <Component key={i} {...c} path={[ ...path, 'children', i ]} />)}</div>
      </Flexbox>
    </CardActions>
  </div>
)

const findComponentIndex = (options, value) => {
  const i = options.findIndex((option) => value === option.label)
  return (i === -1) ? 0 : i
}

const Selector = ({ id, path = [], value, label, description, options = [] }) => {
  const i = findComponentIndex(options, value)
  return (
    <div>
      <Component
        path={[]}
        type='STRING_ENUM'
        value={value || options[0].label}
        defaults={options.map((o) => o.label)} />
      <Component path={[ ...path, 'options', i, 'component' ]}
        {...options[i].component} />
    </div>
  )
}

const inputs = {
  PORT: PortInput,
  HOSTNAME: HostNameInput,
  STRING_ENUM: StringEnumInput,
  STRING: StringInput,
  PASSWORD: PasswordInput,
  INFO: Info,
  PANEL: Panel,
  SELECTOR: Selector
}

const StatelessComponent = ({ type, ...args }) => {
  if (type === undefined) return null

  const found = inputs[type]
  if (found !== undefined) {
    return <div>{React.createElement(found, { ...args })}</div>
  } else {
    return <div>Unknown input type {JSON.stringify(type)}.</div>
  }
}

const mapStateToProps = (state) => ({ submitting: isSubmitting(state) })

Component = connect(mapStateToProps, { onEdit: edit })(StatelessComponent)

export default Component

