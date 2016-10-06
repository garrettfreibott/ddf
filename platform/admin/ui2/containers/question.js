import React from 'react'

import MenuItem from 'material-ui/MenuItem'
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField'

import AutoComplete from 'material-ui/AutoComplete'

import { edit } from '../actions'
import { connect } from 'react-redux'

const PortInput = ({ submitting, id, value, label, error, defaults = [], onEdit }) => (
  <AutoComplete
    dataSource={defaults.map((value) => ({ text: String(value), value: value }))}
    openOnFocus
    filter={AutoComplete.noFilter}
    errorText={error}
    disabled={submitting}
    floatingLabelText={label}
    type='number'
    value={value}
    onNewRequest={({ value }) => { onEdit(id, value) }}
    onUpdateInput={(value) => { onEdit(id, Number(value)) }} />
)

const StringEnumInput = ({ submitting, id, value, label, error, defaults = [], onEdit }) => (
  <SelectField
    value={value || defaults[0] || ''}
    disabled={submitting}
    errorText={error}
    floatingLabelText={label}
    onChange={(e, i, v) => onEdit(id, v)}>
    {defaults.map((d, i) => <MenuItem key={i} value={d} primaryText={d} />)}
  </SelectField>
)

const HostNameInput = ({ submitting, id, value, error, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText='Hostname'
    onChange={(e) => onEdit(id, e.target.value)} />
)

const PasswordInput = ({ submitting, id, label, error, value, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText={label}
    type='password'
    onChange={(e) => onEdit(id, e.target.value)} />
)

const StringInput = ({ submitting, id, label, error, value, onEdit }) => (
  <TextField
    disabled={submitting}
    errorText={error}
    value={value || ''}
    floatingLabelText={label}
    onChange={(e) => onEdit(id, e.target.value)} />
)

const inputs = {
  PORT: PortInput,
  HOSTNAME: HostNameInput,
  STRING_ENUM: StringEnumInput,
  STRING: StringInput,
  PASSWORD: PasswordInput
}

const Question = ({ type, ...args }) => {
  const found = inputs[type]
  if (found !== undefined) {
    return <div>{React.createElement(found, { ...args })}</div>
  } else {
    return <div>Unknown input type {JSON.stringify(type)}.</div>
  }
}

const mapStateToProps = ({ submitting }) => ({ submitting })

export default connect(mapStateToProps, { onEdit: edit })(Question)
