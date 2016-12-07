import React from 'react';
import Paper from 'material-ui/Paper';

import { connect } from 'react-redux'

import { getBins, getNewBins } from '../../reducer'
import { removeContextPath, removeBin, addNewBin, addContextPath, editNewContextPath } from './actions'

import Flexbox from 'flexbox-react'
import IconButton from 'material-ui/IconButton';
import TextField from 'material-ui/TextField';
import Divider from 'material-ui/Divider';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';

import CancelIcon from 'material-ui/svg-icons/content/remove-circle-outline'
import CloseIcon from 'material-ui/svg-icons/action/delete'
import AddIcon from 'material-ui/svg-icons/content/add-circle-outline'
import ContentAdd from 'material-ui/svg-icons/content/add';


import { contextPolicyStyle, infoTitle, infoSubtitle, infoContextPaths } from './styles.less'

let Info = ({id, name, realm, authTypes, reqAttr, binNumber, removeBin}) => (
  <div style={{ width: '100%' }}>
    <p id={id} className={infoTitle}>{name}</p>
    <p id={id} className={infoSubtitle}>Realm: {realm}</p>
    <p id={id} className={infoContextPaths}>Context Paths</p>
    <div style={{ position: 'absolute', top: 0, right: 0 }}>
      <IconButton tooltip={'Delete Bin'} tooltipPosition='top-center' onClick={removeBin}><CloseIcon /></IconButton>
    </div>
  </div>
)
Info = connect(null, (dispatch, { binNumber }) => ({ removeBin: () => dispatch(removeBin(binNumber)) }))(Info)

let ContextPathItem = ({ contextPath, binNumber, pathNumber, removePath }) => (
  <div>
    <Divider />
      <Flexbox flexDirection='row' justifyContent='space-between'>
      <span className={contextPolicyStyle}>{contextPath}</span>
      <IconButton tooltip={'Remove Path'} tooltipPosition='top-center' onClick={removePath}><CancelIcon /></IconButton>
    </Flexbox>
  </div>
)
ContextPathItem = connect(null, (dispatch, { binNumber, pathNumber }) => ({ removePath: () => dispatch(removeContextPath(binNumber, pathNumber)) }))(ContextPathItem)

let NewContextPathItem = ({ binNumber, addPath, onEdit, newPath }) => (
  <div>
    <Divider />
    <Flexbox flexDirection='row' justifyContent='space-between'>
      <TextField style={{ paddingLeft: '10px' }} id='name' hintText='Add New Path' onChange={(event, value) => onEdit(value)} value={newPath || ''} />
      <IconButton tooltip={'Add This Path'} tooltipPosition='top-center' onClick={addPath}><AddIcon /></IconButton>
    </Flexbox>
  </div>
)
NewContextPathItem = connect(null, (dispatch, { binNumber }) => ({
  addPath: () => dispatch(addContextPath(binNumber)),
  onEdit: (value) => dispatch(editNewContextPath(binNumber, value))
}))(NewContextPathItem)

const PolicyBin = ({ policyBin, binNumber }) => (
  <Paper style={{ position: 'relative', width: '300px', padding: '5px', margin: '5px' }}>
    <Info name={policyBin.name} realm={policyBin.realm} binNumber={binNumber} authTypes={policyBin.authTypes} reqAttr={policyBin.reqAttr} />
    <Flexbox flexDirection='column'>
      {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
      <NewContextPathItem binNumber={binNumber} newPath={policyBin.newPath} />
    </Flexbox>
  </Paper>
)

const NewPolicyBin = ({ policyBin, binNumber }) => (
  <Paper style={{ position: 'relative', width: '300px', padding: '5px', margin: '5px' }}>
    <Flexbox flexDirection='column' style={{ width: '100%' }} alignItems='center'>
      <TextField id='name' floatingLabelText='Bin Name' />
      <SelectField id='realm' floatingLabelText='Realm'>
        <MenuItem value='Karaf' primaryText='Karaf' />
        <MenuItem value='LDAP' primaryText='LDAP' />
        <MenuItem value='IDP' primaryText='IDP' />
      </SelectField>
      <p  className={infoContextPaths}>Context Paths</p>
    </Flexbox>
    <Flexbox flexDirection='column'>
      {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
    </Flexbox>
  </Paper>
)

let PolicyBins = ({ policies, newPolicies }) => (
  <Flexbox flexDirection='row' flexWrap='wrap' style={{ width: '100%' }}>
    { policies.map((policyBin, binNumber) => (<PolicyBin policyBin={policyBin} key={binNumber} binNumber={binNumber} />)) }
    { newPolicies.map((policyBin, binNumber) => (<NewPolicyBin policyBin={policyBin} key={binNumber} binNumber={binNumber} />)) }
    <NewBin />
  </Flexbox>
)
PolicyBins = connect((state) => ({ policies: getBins(state), newPolicies: getNewBins(state) }))(PolicyBins)


let NewBin = ({ policies, addNewBin }) => (
  <Paper style={{ position: 'relative', width: '300px', height: '300px', padding: '5px', margin: '5px', textAlign: 'center', backgroundColor: "#EEE" }}>
    <Flexbox style={{ height: '100%', }} flexDirection='column' justifyContent='center' alignItems='center'>
      <FloatingActionButton onClick={addNewBin}>
        <ContentAdd />
      </FloatingActionButton>
    </Flexbox>
  </Paper>
)
NewBin = connect(null, { addNewBin })(NewBin)

export default ({}) => (
  <div style={{ width: '100%', height: '100%', color: 'white', backgroundColor: '#EEE' }}>
    <PolicyBins />
  </div>
)