import React from 'react'

import MenuItem from 'material-ui/MenuItem'
import SelectField from 'material-ui/SelectField'
import TextField from 'material-ui/TextField'

import { edit } from '../actions'
import { connect } from 'react-redux'

const PortInput = ({ submitting, id, value, label, defaults = [], onEdit }) => (
  <TextField
    disabled={submitting}
    value={value}
    floatingLabelText={label}
    onChange={(e) => onEdit(id, e.target.value)} />
)

const StringEnumInput = ({ submitting, id, value, label, defaults = [], onEdit }) => (
  <SelectField
    value={value || defaults[0]}
    disabled={submitting}
    floatingLabelText={label}
    onChange={(e, i, v) => onEdit(id, v)}>
    {defaults.map((d, i) => <MenuItem key={i} value={d} primaryText={d} />)}
  </SelectField>
)

const HostNameInput = ({ submitting, id, value, onEdit }) => (
  <TextField
    disabled={submitting}
    value={value}
    floatingLabelText='Hostname'
    onChange={(e) => onEdit(id, e.target.value)} />
)

const inputs = {
  PORT: PortInput,
  HOSTNAME: HostNameInput,
  STRING_ENUM: StringEnumInput
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
