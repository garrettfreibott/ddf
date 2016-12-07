import { combineReducers } from 'redux-immutable'
import { fromJS, List } from 'immutable'

const mockPolicies = [
  {
    name: 'Admin',
    realm: 'Karaf',
    authTypes: [
      'authType1',
      'authType2'
    ],
    reqAttr: [
      'Attr1',
      'Attr2'
    ],
    contextPaths: [
      '/policyManager',
      '/admin',
      '/somewhere'
    ]
  },
  {
    name: 'Default',
    realm: 'LDAP',
    authTypes: [
      'authType3',
      'authType4'
    ],
    reqAttr: [
      'Attr3',
      'Attr4'
    ],
    contextPaths: [
      '/'
    ],
  },
  {
    name: 'Whitelist',
    realm: 'IDP',
    authTypes: [
      'authType5',
      'authType6'
    ],
    reqAttr: [
      'Attr5',
      'Attr6'
    ],
    contextPaths: [
      '/wizards',
      '/search'
    ]
  }
]

const bins = (state = fromJS(mockPolicies), { type, bin, path, binNumber, pathNumber, value }) => {
  switch (type) {
    case 'WCPM_ADD_BIN':
      return state.push(bin)
    case 'WCPM_REMOVE_BIN':
      return state.delete(binNumber)
    case 'WCPM_ADD_PATH':
      return state.update(binNumber, (bin) => bin.update('contextPaths', (paths) => paths.push(bin.get('newPath'))).set('newPath', ''))
    case 'WCPM_REMOVE_PATH':
      return state.deleteIn([binNumber, 'contextPaths', pathNumber])
    case 'WCPM_EDIT_CONTEXT_PATH':
      return state.setIn([binNumber, 'newPath'], value)
    default:
      return state
  }
}

const emptyBin = ({
  name: '',
  realm: '',
  authTypes: [],
  reqAttr: [],
  contextPaths: []
})

const newBins = (state = List(), { type, bin, path, binNumber, pathNumber }) => {
  switch (type) {
    case 'WCPM_ADD_NEW_BIN':
      return state.push(bin)
    case 'WCPM_ADD_EMPTY_BIN':
      return state.push(emptyBin)
    case 'WCPM_REMOVE_NEW_BIN':
      return state.delete(binNumber)
    case 'WCPM_ADD_NEW_PATH':
      return state.updateIn([binNumber, 'contextPaths'], (paths) => paths.push(path))
    case 'WCPM_REMOVE_NEW_PATH':
      return state.deleteIn([binNumber, 'contextPaths', pathNumber])
    default:
      return state
  }
}

export const getBins = (state) => state.getIn(['bins']).toJS()
export const getNewBins = (state) => state.getIn(['newBins']).toJS()

export default combineReducers({ bins, newBins })