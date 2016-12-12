import React from 'react';
import Paper from 'material-ui/Paper';

import { connect } from 'react-redux'

import {
  getBins,
  getEditBins,
  getOptions
} from '../../reducer'

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
  policyBinInnerStyle,
  editPaneStyle,
  newBinStyle,
  infoSubtitleLeft
} from './styles.less'

export const InfoField = ({ id, label, value }) => (
  <div style={{ fontSize: '16px', lineHeight: '24px', width: '100%', display: 'inline-block', position: 'relative', height: '200ms' }}>
    <label htmlFor={id} style={{ color: 'rgba(0, 0, 0, 0.541176)', position: 'absolute', lineHeight: '22px', top: '30px', transform: 'scale(0.75) translate(10px, -28px)', transformOrigin: 'left top 0px' }}>{label}</label>
    <p id={id} style={{ position: 'relative', height: '100%', margin: '28px 10px 7px', whiteSpace: 'nowrap' }}>{value}</p>
  </div>
)

let Info = ({id, name, realm, authTypes, reqAttr, binNumber, editModeOn}) => (
  <div style={{ width: '100%' }}>
    <Flexbox className={editPaneStyle} justifyContent='center' alignItems='center' onClick={editModeOn}>
      <IconButton tooltip={'Edit Attributes'} tooltipPosition='bottom-center'><EditModeIcon /></IconButton>
    </Flexbox>
  </div>
)
Info = connect(null, (dispatch, { binNumber }) => ({ editModeOn: () => dispatch(editModeOn(binNumber)) }))(Info)

let EditInfo = ({id, bin, binNumber, editModeCancel}) => {
  const title = (bin.beforeEdit !== undefined) ? '*Editing bin: '+ bin.beforeEdit.name + '*' : '*Creating New Bin*'
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
      <TextField fullWidth style={{ paddingLeft: '10px' }} id='name' hintText='Add New Path' onChange={(event, value) => onEdit(value)} value={newPath || ''} />
      <IconButton tooltip={'Add This Path'} tooltipPosition='top-center' onClick={addPath}><AddIcon /></IconButton>
    </Flexbox>
  </div>
)
NewContextPathItem = connect(null, (dispatch, { binNumber, attribute }) => ({
  addPath: () => dispatch(addAttribute(attribute)(binNumber)),
  onEdit: (value) => dispatch(editAttribute(attribute)(binNumber, value))
}))(NewContextPathItem)

let NewSelectItem = ({ binNumber, addPath, onEdit, newPath, attribute, options }) => (
  <div>
    <Divider />
    <Flexbox flexDirection='row' justifyContent='space-between'>
      <SelectField fullWidth style={{ paddingLeft: '10px' }} id='name' hintText='Add New Path' onChange={(event, i, value) => onEdit(value)} value={newPath || ''}>
        { options.map((item, key) => (<MenuItem value={item} key={key} primaryText={item} />)) }
      </SelectField>
      <IconButton tooltip={'Add This Path'} tooltipPosition='top-center' onClick={addPath}><AddIcon /></IconButton>
    </Flexbox>
  </div>
)
NewSelectItem = connect(null, (dispatch, { binNumber, attribute }) => ({
  addPath: () => dispatch(addAttribute(attribute)(binNumber)),
  onEdit: (value) => dispatch(editAttribute(attribute)(binNumber, value))
}))(NewSelectItem)

const PolicyBin = ({ policyBin, binNumber }) => (
  <Paper className={policyBinOuterStyle} >
    <Flexbox flexDirection='row'>
      <Flexbox style={{ width: '20%', padding: '5px', borderRight: '1px solid grey' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Context Paths</p>
        {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <Divider />
      </Flexbox>
      <Flexbox style={{ width: '30%', padding: '5px' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Realm</p>
        <Divider/>
        <p className={contextPolicyStyle}>{policyBin.realm}</p>
        <p className={infoSubtitleLeft}>Authentication Types</p>
        <Flexbox flexDirection='column'>
          {policyBin.authTypes.map((authType, pathNumber) => (<ContextPathItem editing={false} attribute='authTypes' key={pathNumber} contextPath={authType} binNumber={binNumber} pathNumber={pathNumber}/>))}
        </Flexbox>
      </Flexbox>
      <Flexbox style={{ width: '50%',  padding: '5px' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Required Attributes</p>
        <Flexbox flexDirection='column'>
          {policyBin.reqAttr.map((reqAttr, pathNumber) => (<ContextPathItem editing={false} attribute='reqAttr' key={pathNumber} contextPath={reqAttr} binNumber={binNumber} pathNumber={pathNumber}/>))}
        </Flexbox>
      </Flexbox>
    </Flexbox>
    <Info name={policyBin.name} realm={policyBin.realm} binNumber={binNumber} authTypes={policyBin.authTypes} reqAttr={policyBin.reqAttr} />
  </Paper>
)

let EditPolicyBin = ({ policyBin, policyOptions, binNumber, removeBin, editModeSave, editModeCancel, editRealm, editName }) => (
  <Paper className={policyBinOuterStyle} >
    <Flexbox flexDirection='row'>
      <Flexbox style={{ width: '20%', padding: '5px', borderRight: '1px solid grey' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Context Paths</p>
        {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='contextPaths' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='contextPaths' newPath={policyBin['newcontextPaths']} />
      </Flexbox>
      <Flexbox style={{ width: '30%', padding: '5px', borderRight: '1px solid grey' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Realm</p>
        <Divider />
        <SelectField style={{ margin: '0px 10px' }} id='realm' value={policyBin.realm} onChange={(event, i, value) => editRealm(value)}>
          {policyOptions.realms.map((realm, i) => (<MenuItem value={realm} primaryText={realm} key={i} />))}
        </SelectField>
        <p className={infoSubtitleLeft}>Authentication Types</p>
        <Flexbox flexDirection='column'>
          {policyBin.authTypes.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='authTypes' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
          <NewSelectItem binNumber={binNumber} attribute='authTypes' options={policyOptions.authTypes.filter((option) => !policyBin.authTypes.includes(option))} newPath={policyBin['newauthTypes']} />
        </Flexbox>
      </Flexbox>
      <Flexbox style={{ width: '50%',  padding: '5px' }} flexDirection='column'>
        <p className={infoSubtitleLeft}>Required Attributes</p>
        <Flexbox flexDirection='column'>
          {policyBin.reqAttr.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='reqAttr' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
          <NewContextPathItem binNumber={binNumber} attribute='reqAttr' newPath={policyBin['newreqAttr']} />        </Flexbox>
      </Flexbox>
    </Flexbox>
    <Divider />
    <Flexbox flexDirection='row' justifyContent='center' style={{ padding: '10px 0px 5px' }}>
      <FlatButton style={{ margin: '0 10'}} label="Cancel" labelPosition="after" secondary={true} onClick={editModeCancel} />
      <RaisedButton style={{ margin: '0 10'}} label="Done" primary={true} onClick={editModeSave} />
      <IconButton style={{ position: 'absolute', right: '0px', margin: '0 10'}} onClick={removeBin} secondary={true} tooltip={'Delete Bin'} tooltipPosition='top-center' ><DeleteIcon /></IconButton>
    </Flexbox>
  </Paper>
  /*
  <Paper className={policyBinOuterStyle}>
    <Paper className={policyBinInnerStyle}>
      <Flexbox flexDirection='column' style={{ width: '100%' }} alignItems='center'>
        <EditInfo binNumber={binNumber} bin={policyBin} />
        <TextField id='name' floatingLabelText='Bin Name' value={policyBin.name} onChange={(event, value) => editName(value)}/>
        <SelectField id='realm' floatingLabelText='Realm' value={policyBin.realm} onChange={(event, i, value) => editRealm(value)}>
          {policyOptions.realms.map((realm, i) => (<MenuItem value={realm} primaryText={realm} key={i} />))}
        </SelectField>
      </Flexbox>
      <div style={{ backgroundColor: '#EEE' }}>
        <p  className={infoContextPaths}>Authentication Types</p>
      </div>
      <Flexbox flexDirection='column'>
        {policyBin.authTypes.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='authTypes' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewSelectItem binNumber={binNumber} attribute='authTypes' options={policyOptions.authTypes} newPath={policyBin['newauthTypes']} />
      </Flexbox>
      <div style={{ backgroundColor: '#EEE' }}>
        <p  className={infoContextPaths}>Required Attributes</p>
      </div>
      <Flexbox flexDirection='column'>
        {policyBin.reqAttr.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='reqAttr' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='reqAttr' newPath={policyBin['newreqAttr']} />
      </Flexbox>
      <div style={{ backgroundColor: '#EEE' }}>
        <p  className={infoContextPaths}>Context Paths</p>
      </div>
      <Flexbox flexDirection='column'>
        {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem editing={true} attribute='contextPaths' contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
        <NewContextPathItem binNumber={binNumber} attribute='contextPaths' newPath={policyBin['newcontextPaths']} />
      </Flexbox>
      <Divider />
      <Flexbox flexDirection='row' justifyContent='center' style={{ padding: '10px 0px 5px' }}>
        <FlatButton style={{ margin: '0 10'}} label="Delete" labelPosition="after" icon={<DeleteIcon />} secondary={true} onClick={removeBin} />
        <RaisedButton style={{ margin: '0 10'}} label="Done" primary={true} onClick={editModeSave} />
      </Flexbox>
    </Paper>
  </Paper>
  */
)
EditPolicyBin = connect(
  (state) => ({
    policyOptions: getOptions(state)
  }),
  (dispatch, { binNumber }) => ({
    removeBin: () => dispatch(removeBin(binNumber)),
    editModeSave: () => dispatch(editModeSave(binNumber)),
    editModeCancel: () => dispatch(editModeCancel(binNumber)),
    editRealm: (value) => dispatch(editRealm(binNumber, value)),
}))(EditPolicyBin)

let PolicyBins = ({ policies }) => (
  <Flexbox style={{ height: '100%', width: '100%', overflowY: 'scroll', padding: '0px 5px', boxSizing: 'border-box' }} flexDirection='column' alignItems='center' >
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
  <Paper style={{ position: 'relative', width: '100%', height: '100px', margin: '5px 0px', textAlign: 'center', backgroundColor: "#EEE" }} onClick={addNewBin}>
    <Flexbox className={newBinStyle} flexDirection='column' justifyContent='center' alignItems='center'>
      <FloatingActionButton>
        <ContentAdd />
      </FloatingActionButton>
    </Flexbox>
  </Paper>
)
NewBin = connect(null, { addNewBin })(NewBin)

export default ({}) => (
  <Flexbox flexDirection='column' style={{ width: '100%', height: '100%' }}>
    <div style={{ width: '100%' }}>
      <p className={infoTitle}>Web Context Policy Manager</p>
      <p className={infoSubtitle}>This is the web context policy manager. Hopefully it's nicer to use than the other one.</p>
    </div>
    <Divider/>
    <PolicyBins/>
  </Flexbox>
)