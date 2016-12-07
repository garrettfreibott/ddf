import React from 'react';
import Paper from 'material-ui/Paper';

import { connect } from 'react-redux'

import { getPolicies } from '../../reducer'
import { removeContextPath, removeBin } from './actions'

import Flexbox from 'flexbox-react'

import IconButton from 'material-ui/IconButton';
import CancelIcon from 'material-ui/svg-icons/navigation/cancel'
import CloseIcon from 'material-ui/svg-icons/navigation/close'
import Divider from 'material-ui/Divider';

import { contextPolicyStyle, infoTitle, infoSubtitle, infoContextPaths } from './styles.less'

let Info = ({id, name, realm, binNumber, removeBin}) => (
  <div style={{ width: '100%' }}>
    <p id={id} className={infoTitle}>{name}</p>
    <p id={id} className={infoSubtitle}>Realm: {realm}</p>
    <Divider />
    <p id={id} className={infoContextPaths}>Context Paths</p>
    <div style={{ position: 'absolute', top: 0, right: 0 }}>
      <IconButton tooltip={'Remove Path'} tooltipPosition='top-center' onClick={removeBin}><CloseIcon /></IconButton>
    </div>
  </div>
)
Info = connect(null, (dispatch, { binNumber }) => ({ removeBin: () => dispatch(removeBin(binNumber)) }))(Info)

const PolicyGroupBar = ({ }) => (
  <div style={{ width: '100%', height: '30px', backgroundColor: 'darkgreen' }}>
    New Policy Group Bar
  </div>
)

const ContextPathBar = ({ }) => (
  <div style={{ width: '100%', height: '30px', backgroundColor: 'green' }}>
    Add Context Path Bar
  </div>
)

let ContextPathItem = ({ contextPath, binNumber, pathNumber, removePath }) => (
  <Flexbox style={{ backgroundColor:'#DDD', border:'1px solid lightgrey' }} flexDirection='row' justifyContent='space-between'>
    <span className={contextPolicyStyle}>{contextPath}</span>
    <IconButton tooltip={'Remove Path'} tooltipPosition='top-center' onClick={removePath}><CancelIcon /></IconButton>
  </Flexbox>
)
ContextPathItem = connect(null, (dispatch, { binNumber, pathNumber }) => ({ removePath: () => dispatch(removeContextPath(binNumber, pathNumber)) }))(ContextPathItem)

const PolicyBin = ({ policyBin, binNumber }) => (
  <Paper style={{ position: 'relative', width: '300px', padding: '5px', margin: '5px' }}>
    <Info name={policyBin.name} realm={policyBin.realm} binNumber={binNumber} />
    <Flexbox flexDirection='column'>
      {policyBin.contextPaths.map((contextPath, pathNumber) => (<ContextPathItem contextPath={contextPath} key={pathNumber} binNumber={binNumber} pathNumber={pathNumber} />))}
    </Flexbox>
  </Paper>
)

let PolicyBins = ({ policies }) => (
  <Flexbox flexDirection='row' flexWrap='wrap' style={{ width: '100%' }}>
    { policies.map((policyBin, binNumber) => (<PolicyBin policyBin={policyBin} key={binNumber} binNumber={binNumber} />)) }
  </Flexbox>
)
PolicyBins = connect((state) => ({policies: getPolicies(state) }))(PolicyBins)



export default ({}) => (
  <div style={{ width: '100%', height: '100%', color: 'white', backgroundColor: 'lightgreen' }}>
    <PolicyGroupBar />
    <ContextPathBar />
    <PolicyBins />
  </div>
)