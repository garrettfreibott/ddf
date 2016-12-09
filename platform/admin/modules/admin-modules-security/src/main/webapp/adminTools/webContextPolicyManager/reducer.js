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

const emptyEditBin = fromJS({
  name: 'untitled',
  realm: '',
  authTypes: [],
  reqAttr: [],
  contextPaths: [],
  editing: true
})


const bins = (state = fromJS(mockPolicies), { type, bin, path, binNumber, pathNumber, value, attribute }) => {
  switch (type) {
    // Bin Level
    case 'WCPM_ADD_BIN':
      return state.push(emptyEditBin)
    case 'WCPM_REMOVE_BIN':
      return state.delete(binNumber)
    case 'WCPM_EDIT_MODE_ON':
      return state.update(binNumber, (bin) => bin.merge({ beforeEdit: bin, editing: true }))
    case 'WCPM_EDIT_MODE_CANCEL':
      if (state.hasIn([binNumber, 'beforeEdit'])) {
        return state.update(binNumber, (bin) => bin.get('beforeEdit'))
      } else {
        return state.delete(binNumber)
      }
    case 'WCPM_EDIT_MODE_SAVE':
      return state.update(binNumber, (bin) => bin.delete('beforeEdit').merge({ editing: false }))

    // Name
    case 'WCPM_EDIT_NAME':
      return state.setIn([binNumber, 'name'], value)

    // Realm
    case 'WCPM_EDIT_REALM':
      return state.setIn([binNumber, 'realm'], value)

    // Attribute Lists
    case 'WCPM_ADD_ATTRIBUTE_LIST':
      return state.update(binNumber, (bin) => bin.update(attribute, (paths) => paths.push(bin.get('new' + attribute))).set('new' + attribute, ''))
    case 'WCPM_REMOVE_ATTRIBUTE_LIST':
      return state.deleteIn([binNumber, attribute, pathNumber])
    case 'WCPM_EDIT_ATTRIBUTE_LIST':
      return state.setIn([binNumber, 'new' + attribute], value)

    default:
      return state
  }
}

export const getBins = (state) => state.getIn(['bins']).toJS()

export default combineReducers({ bins })