import React from 'react'

import MenuItem from 'material-ui/MenuItem'
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField'

import AutoComplete from 'material-ui/AutoComplete'

import { edit } from '../actions'
import { connect } from 'react-redux'

import { isSubmitting } from '../reducer'

const PortInput = ({ submitting, id, value, error, defaults = [], onEdit }) => (
  <AutoComplete
    dataSource={defaults.map((value) => ({ text: String(value), value: value }))}
    openOnFocus
    filter={AutoComplete.noFilter}
    type='number'
    errorText={error}
    disabled={submitting}
    floatingLabelText='Port'
    searchText={String(value || defaults[0])}
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

const Info = ({id, label, value}) => (
    <div>
        <h3>{label}</h3>
        <p>{value}</p>
    </div>
)

const inputs = {
  PORT: PortInput,
  HOSTNAME: HostNameInput,
  STRING_ENUM: StringEnumInput,
  STRING: StringInput,
  PASSWORD: PasswordInput,
  INFO: Info
}

const Question = ({ type, ...args }) => {
  const found = inputs[type]
  if (found !== undefined) {
    return <div>{React.createElement(found, { ...args })}</div>
  } else {
    return <div>Unknown input type {JSON.stringify(type)}.</div>
  }
}

const mapStateToProps = (state) => ({ submitting: isSubmitting(state) })

export default connect(mapStateToProps, { onEdit: edit })(Question)
