import { combineReducers } from 'redux-immutable'
import { fromJS, List } from 'immutable'

const mockPolicies = [
  {
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

const mockClaims = ({
  realms: [
    'Karaf',
    'LDAP',
    'IDP'
  ],
  authTypes:[
    'Karaf',
    'Basic',
    'Guest',
    'LDAP',
    'IDP',
    'SAML'
  ],
  claims: [
    '{http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role=system-user}',
    '{http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role=system-admin}',
    '{http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role=system-other}'
  ]
})

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

const options = (state = fromJS(mockClaims), { type, claims }) => {
  switch (type) {
    case 'WCPM_SET_OPTIONS':
      return fromJS(claims)
    default:
      return state
  }
}

export const getOptions = (state) => state.get('options').toJS()
export const getBins = (state) => state.get('bins').toJS()



export default combineReducers({ bins, options })