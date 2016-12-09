import React from 'react';
import Paper from 'material-ui/Paper';

import { connect } from 'react-redux'

import { getBins, getEditBins } from '../../reducer'

import {
  removeBin,
  addAttribute,
  editAttribute,
  removeAttribute,
  addNewBin,
  editModeOn,
  editModeCancel,
  editModeSave,
  editName,
  editRealm
} from './actions'

import Flexbox from 'flexbox-react'
import IconButton from 'material-ui/IconButton';
import TextField from 'material-ui/TextField';
import Divider from 'material-ui/Divider';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';

import CancelIcon from 'material-ui/svg-icons/content/remove-circle-outline'
import CloseIcon from 'material-ui/svg-icons/navigation/close'
import DeleteIcon from 'material-ui/svg-icons/action/delete'
import AddIcon from 'material-ui/svg-icons/content/add-circle-outline'
import ContentAdd from 'material-ui/svg-icons/content/add';
import EditModeIcon from 'material-ui/svg-icons/editor/mode-edit'


import {
  contextPolicyStyle,
  infoTitle,
  infoSubtitle,
  infoContextPaths,
  policyBinStyle,
  policyBinOuterStyle,
  policyBinInnerStyle
} from './styles.less'

let Info = ({id, name, realm, authTypes, reqAttr, binNumber, editModeOn}) => (
  <div style={{ width: '100%' }}>
    <p id={id} className={infoTitle}>{name}</p>
      <div style={{ position: 'absolute', top: 0, right: 0 }}>
      <IconButton tooltip={'Edit Attributes'} tooltipPosition='bottom-center' onClick={editModeOn}><EditModeIcon /></IconButton>
    </div>
  </div>
)
Info = connect(null, (dispatch, { binNumber }) => ({ editModeOn: () => dispatch(editModeOn(binNumber)) }))(Info)

let EditInfo = ({id, bin, binNumber, editModeCancel}) => {
  const title = (bin.beforeEdit !== undefined) ? '*Editing bin: '+ bin.name + '*' : '*Creating New Bin*'
  return (
    <div style={{ width: '100%', backgroundColor: '#DDD' }}>
      <p id={id} className={infoTitle}>{title}</p>
      <div style={{ position: 'absolute', top: 0, right: 0 }}>
        <IconButton tooltip={'Cancel'} tooltipPosition='bottom-center' onClick={editModeCancel}><CloseIcon /></IconButton>
      </div>
    </div>
  )
}
EditInfo = connect(null, (dispatch, { binNumber }) => ({ editModeCancel: () => dispatch(editModeCancel(binNumber)) }))(EditInfo)

let ContextPathItem = ({ contextPath, binNumber, pathNumber, removePath, editing, attribute }) => (
  <div>
    <Divider />
      <Flexbox flexDirection='row' justifyContent='space-between'>
        <span className={contextPolicyStyle}>{contextPath}</span>
        {editing ? (<IconButton tooltip={'Add This Path'} tooltipPosition='top-center' onClick={removePath}><CancelIcon /></IconButton>) : null}
    </Flexbox>
  </div>
)
ContextPathItem = connect(null, (dispatch, { binNumber, pathNumber, attribute }) => ({ removePath: () => dispatch(removeAttribute(attribute)(binNumber, pathNumber))}))(ContextPathItem)


let NewContextPathItem = ({ binNumber, addPath, onEdit, newPath, attribute }) => (
  <div>
    <Divider />
    <Flexbox flexDirection='row' justifyContent='space-between'>
      <TextField style={{ paddingLeft: '10px' }} id='name' hintText='Add New Path' onChange={(event, value) => onEdit(value)} value={newPath || ''} />
      <IconButton tooltip={'Add This Path'} tooltipPosition='top-center' onClick={addPath}><AddIcon /></IconButton>
    </Flexbox>
  </div>
)
NewContextPathItem = connect(null, (dispatch, { binNumber, attribute }) => ({
  addPath: () => dispatch(addAttribute(attribute)(binNumber)),
  onEdit: (value) => dispatch(editAttribute(attribute)(binNumber, value))
}))(NewContextPathItem)

const PolicyBin = ({ policyBin, binNumber }) => (
  <Paper className={policyBinOuterStyle} >
    <Paper className={policyBinInnerStyle}>
      <Info name={policyBin.name} realm={policyBin.realm} binNumber={binNumber} authTypes={policyBin.authTypes} reqAttr={policyBin.reqAttr} />
      <Flexbox flexDirection='column'>
        {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
      </Flexbox>
    </Paper>
  </Paper>
)

let EditPolicyBin = ({ policyBin, binNumber, removeBin, editModeSave, editRealm, editName }) => (
  <Paper className={policyBinOuterStyle}>
    <Paper className={policyBinInnerStyle}>
      <Flexbox flexDirection='column' style={{ width: '100%' }} alignItems='center'>
        <EditInfo binNumber={binNumber} bin={policyBin} />
        <TextField id='name' floatingLabelText='Bin Name' value={policyBin.name} onChange={(event, value) => editName(value)}/>
        <SelectField id='realm' floatingLabelText='Realm' value={policyBin.realm} onChange={(event, i, value) => editRealm(value)}>
          <MenuItem value='Karaf' primaryText='Karaf' />
          <MenuItem value='LDAP' primaryText='LDAP' />
          <MenuItem value='IDP' primaryText='IDP' />
        </SelectField>
      </Flexbox>
      <p  className={infoContextPaths}>Context Paths</p>
      <Flexbox flexDirection='column'>
        {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='contextPaths' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='contextPaths' newPath={policyBin['newcontextPaths']} />
      </Flexbox>
      <p  className={infoContextPaths}>Authentication Types</p>
      <Flexbox flexDirection='column'>
        {policyBin.authTypes.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='authTypes' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='authTypes' newPath={policyBin['newauthTypes']} />
      </Flexbox>
      <p  className={infoContextPaths}>Required Attributes</p>
      <Flexbox flexDirection='column'>
        {policyBin.reqAttr.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='reqAttr' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='reqAttr' newPath={policyBin['newreqAttr']} />
      </Flexbox>
      <Divider />
      <Flexbox flexDirection='row' justifyContent='center' style={{ padding: '10px 0px 5px' }}>
        <FlatButton style={{ margin: '0 10'}} label="Delete" labelPosition="after" icon={<DeleteIcon />} secondary={true} onClick={removeBin} />
        <RaisedButton style={{ margin: '0 10'}} label="Done" primary={true} onClick={editModeSave} />
      </Flexbox>
    </Paper>
  </Paper>
)
EditPolicyBin = connect(null, (dispatch, { binNumber }) => ({
  removeBin: () => dispatch(removeBin(binNumber)),
  editModeSave: () => dispatch(editModeSave(binNumber)),
  editName: (value) => dispatch(editName(binNumber, value)),
  editRealm: (value) => dispatch(editRealm(binNumber, value)),
}))(EditPolicyBin)

let PolicyBins = ({ policies }) => (
  <Flexbox flexDirection='row' flexWrap='wrap' style={{ width: '100%' }} >
    { policies.map((policyBin, binNumber) => {
      if (policyBin.editing !== undefined && policyBin.editing) {
        return (<EditPolicyBin policyBin={policyBin} key={binNumber} binNumber={binNumber}/>)
      }
      return (<PolicyBin policyBin={policyBin} key={binNumber} binNumber={binNumber}/>)
    })}
    <NewBin />
  </Flexbox>
)
PolicyBins = connect((state) => ({ policies: getBins(state) }))(PolicyBins)


let NewBin = ({ policies, addNewBin }) => (
  <Paper style={{ position: 'relative', width: '250px', height: '250px', padding: '5px', margin: '5px', textAlign: 'center', backgroundColor: "#EEE" }}>
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