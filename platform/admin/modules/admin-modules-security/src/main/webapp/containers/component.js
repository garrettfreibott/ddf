import React from 'react'

import MenuItem from 'material-ui/MenuItem'
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField'
import RaisedButton from 'material-ui/RaisedButton'
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';

import AutoComplete from 'material-ui/AutoComplete'

import { CardActions, CardHeader } from 'material-ui/Card'
import Flexbox from 'flexbox-react'

import { submit, edit } from '../actions'
import { connect } from 'react-redux'

import { isSubmitting } from '../reducer'

import errorStyles from "./errors.less"

var Component

const PortInput = ({ disabled, path, value, error, defaults = [], onEdit }) => (
  <AutoComplete
    dataSource={defaults.map((value) => ({ text: String(value), value: value }))}
    openOnFocus
    filter={AutoComplete.noFilter}
    type='number'
    errorText={error}
    disabled={disabled}
    floatingLabelText='Port'
    searchText={String(value || defaults[0])}
    onNewRequest={({ value }) => { onEdit(path, value) }}
    onUpdateInput={(value) => { onEdit(path, Number(value)) }} />
)

const StringEnumInput = ({ disabled, path, value, label, error, defaults = [], onEdit }) => (
  <SelectField
    value={value || defaults[0] || ''}
    disabled={disabled}
    errorText={error}
    floatingLabelText={label}
    onChange={(e, i, v) => onEdit(path, v)}>
    {defaults.map((d, i) => <MenuItem key={i} value={d} primaryText={d} />)}
  </SelectField>
)

const HostNameInput = ({ disabled, path, value, error, onEdit }) => (
  <TextField
    disabled={disabled}
    errorText={error}
    value={value || ''}
    floatingLabelText='Hostname'
    onChange={(e) => onEdit(path, e.target.value)} />
)

const PasswordInput = ({ disabled, path, label, error, value, onEdit }) => (
  <TextField
    disabled={disabled}
    errorText={error}
    value={value || ''}
    floatingLabelText={label}
    type='password'
    onChange={(e) => onEdit(path, e.target.value)} />
)

const StringInput = ({ disabled, path, label, error, value, onEdit }) => (
  <TextField
    disabled={disabled}
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

const Panel = ({ disabled, id, path = [], label, description, children = [] }) => (
  <div>
    <CardHeader title={label || id} subtitle={description} />
    <CardActions>
      <Flexbox justifyContent='center'>
        <div>{children.map((c, i) =>
          <Component disabled={disabled} key={i} {...c} path={[ ...path, 'children', i ]} />)}</div>
      </Flexbox>
    </CardActions>
  </div>
)

const RadioButtons = ({ id, options = [], defaultSelected }) => (
  <div>
    <RadioButtonGroup name={id} defaultSelected={defaultSelected}>
      {options.map((c) =>
      <RadioButton value={c} label={c} />)}
    </RadioButtonGroup>
  </div>
);

// can be refactored to use findIndex, but don't want to shim it yet
const findComponentIndex = (options, value) => {
  for (var i = 0; i < options.length; i++) {
    if (value === options[i].label) return i
  }
  return 0
}

const Selector = ({ id, path = [], value, label, description, options = [] }) => {
  const i = findComponentIndex(options, value)
  return (
    <div>
      <Component
        path={path}
        type='STRING_ENUM'
        value={value || options[0].label}
        defaults={options.map((o) => o.label)} />
      <Component path={[ ...path, 'options', i, 'component' ]}
        {...options[i].component} />
    </div>
  )
}

const ButtonAction = ({ path, label, disabled, onSubmit, ...rest }) => (
  <div>
    <RaisedButton
      style={{ marginLeft: 10 }}
      onClick={() => { onSubmit(rest) }}
      label={label}
      primary
      disabled={disabled} />
  </div>
)

const ErrorInfo = ({ label }) => (
  <div className={errorStyles.message}>
    <Flexbox justifyContent='space-between'>
      {label}
    </Flexbox>
  </div>
)

const inputs = {
  PORT: PortInput,
  HOSTNAME: HostNameInput,
  STRING_ENUM: StringEnumInput,
  STRING: StringInput,
  PASSWORD: PasswordInput,
  INFO: Info,
  BASE_CONTAINER: Panel,
  SELECTOR: Selector,
  BUTTON_ACTION: ButtonAction,
  RADIO_BUTTONS: RadioButtons,
  ERROR_INFO: ErrorInfo
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

const mapStateToProps = (state, ownProps) => ({ disabled: ownProps.disabled || isSubmitting(state) })

Component = connect(mapStateToProps, { onEdit: edit, onSubmit: submit })(StatelessComponent)

export default Component

