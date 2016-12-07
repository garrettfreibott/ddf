import { combineReducers } from 'redux-immutable'
import { fromJS } from 'immutable'

const mockPolicies = [
  {
    name: 'Admin',
    realm: 'Karaf',
    authType: [
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
    authType: [
      'authType3',
      'authType4'
    ],
    reqAttr: [
      'Attr3',
      'Attr4'
    ],
    contextPaths: [
      '/'
    ]
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

const policies = (state = fromJS(mockPolicies), { type, bin, path, binNumber, pathNumber }) => {
  switch (type) {
    case 'WCPM_ADD_BIN':
      return state.push(bin)
    case 'WCPM_REMOVE_BIN':
      return state.delete(binNumber)
    case 'WCPM_ADD_PATH':
      return state.updateIn([binNumber, 'contextPaths'], (paths) => paths.push(path))
    case 'WCPM_REMOVE_PATH':
      return state.deleteIn([binNumber, 'contextPaths', pathNumber])
    default:
      return state
  }
}

export const getPolicies = (state) => state.getIn(['policies']).toJS()

export default combineReducers({ policies })