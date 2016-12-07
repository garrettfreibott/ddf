import { getBins } from '../../reducer'
import { backendError } from '../../actions'

// Bin level
export const replaceAllBins = (bins) => ({ type: 'REPLACE_ALL_BINS', bins })
export const removeBin = (binNumber) => ({ type: 'WCPM_REMOVE_BIN', binNumber })
export const addNewBin = () => ({ type: 'WCPM_ADD_BIN' })
export const editModeOn = (binNumber) => ({ type: 'WCPM_EDIT_MODE_ON', binNumber })
export const editModeCancel = (binNumber) => ({ type: 'WCPM_EDIT_MODE_CANCEL', binNumber })
export const editModeSave = (binNumber) => ({ type: 'WCPM_EDIT_MODE_SAVE', binNumber })

// Realm
export const editRealm = (binNumber, value) => ({ type: 'WCPM_EDIT_REALM', binNumber, value })

// Attribute Lists
export const removeAttribute = (attribute) => (binNumber, pathNumber) => ({ type: 'WCPM_REMOVE_ATTRIBUTE_LIST', attribute, binNumber, pathNumber })
export const addAttribute = (attribute) => (binNumber, path) => ({ type: 'WCPM_ADD_ATTRIBUTE_LIST', attribute, binNumber, path })
export const editAttribute = (attribute) => (binNumber, value) => ({ type: 'WCPM_EDIT_ATTRIBUTE_LIST', attribute, binNumber, value })

// Attribute mapping reducers
export const addAttributeMapping = (binNumber) => ({ type: 'WCPM_ADD_ATTRIBUTE_MAPPING', binNumber })

// Set Options
export const setPolicyOptions = (options) => ({ type: 'WCPM_SET_OPTIONS', options })

// Fetch

export const updatePolicyBins = (url) => (dispatch, getState) => {
  const opts = {
    method: 'GET',
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      if (status === 200) {
        var contextPolicyBins = json[0].contextPolicyBins
        var listOfAttrs = []
        contextPolicyBins.forEach((bin, i) => { listOfAttrs.push([]); Object.keys(bin.requiredAttributes).forEach((key) => bin.requiredAttributes[key].forEach((value) => listOfAttrs[i].push({ claim: key, attribute: value }))) })
        contextPolicyBins.forEach((bin, i) => { bin.requiredAttributes = listOfAttrs[i]; return null })
        dispatch(replaceAllBins(contextPolicyBins))
        dispatch(fetchOptions('/admin/wizard/probe/contextPolicyManager/options'))
      }
    })
    .catch(() => {
//    TODO handle probe errors
    })
}

export const persistChanges = (url) => (dispatch, getState) => {
  let contextPolicyBins = getBins(getState())

  let listOfAttrs = []

  contextPolicyBins.forEach((bin, i) => { listOfAttrs.push({}); bin.requiredAttributes.forEach((reqAttrObj) => listOfAttrs[i][reqAttrObj.claim] ? listOfAttrs[i][reqAttrObj.claim].push(reqAttrObj.attribute) : listOfAttrs[i][reqAttrObj.claim] = [reqAttrObj.attribute]) })

  contextPolicyBins.forEach((bin, i) => bin.requiredAttributes = listOfAttrs[i])

  let formattedBody = [
    {
      configurationType: 'contextPolicyManager',
      contextPolicyBins: contextPolicyBins
    }
  ]

  const opts = {
    method: 'POST',
    body: JSON.stringify(...formattedBody),
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      if (status === 200) {
      } else {
        dispatch(backendError(json))
      }
    })
    .catch(() => {
//    TODO handle probe errors
    })
}

export const fetchOptions = (url) => (dispatch, getState) => {
  let contextPolicyBins = getBins(getState())
  let listOfAttrs = []

  contextPolicyBins.forEach((bin, i) => { listOfAttrs.push({}); bin.requiredAttributes.forEach((reqAttrObj) => listOfAttrs[i][reqAttrObj.claim] ? listOfAttrs[i][reqAttrObj.claim].push(reqAttrObj.attribute) : listOfAttrs[i][reqAttrObj.claim] = [reqAttrObj.attribute]) })
  contextPolicyBins.forEach((bin, i) => bin.requiredAttributes = listOfAttrs[i])

  let formattedBody = [
    {
      configurationType: 'contextPolicyManager',
      contextPolicyBins: contextPolicyBins
    }
  ]

  const opts = {
    method: 'POST',
    body: JSON.stringify(...formattedBody),
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      if (status === 200) {
        dispatch(setPolicyOptions(json.probeResults))
      } else {
      }
    })
    .catch(() => {
//    TODO handle probe errors
    })
}

/*
const mockPolicies = [
  {
    realm: 'Karaf',
    authenticationTypes: [
      'authType1',
      'authType2'
    ],
    requiredAttributes: [
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      },
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      },
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      }
    ],
    contextPaths: [
      '/policyManager',
      '/admin',
      '/somewhere'
    ]
  },
  {
    realm: 'LDAP',
    authenticationTypes: [
      'authType3',
      'authType4'
    ],
    requiredAttributes: [
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      },
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      }
    ],
    contextPaths: [
      '/'
    ],
  },
  {
    realm: 'IDP',
    authenticationTypes: [
      'authType5',
      'authType6'
    ],
    requiredAttributes: [
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      },
      {
        claim: "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role",
        attribute: "system-admin"
      }
    ],
    contextPaths: [
      '/wizards',
      '/search'
    ]
  }
]
*/
