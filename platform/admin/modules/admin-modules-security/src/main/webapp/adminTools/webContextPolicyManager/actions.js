const newBin = (name, realm, authTypes, reqAttr) => ({
  name: name,
  realm: realm,
  authTypes: authTypes,
  reqAttr: reqAttr,
  contextPaths: []
})

export const addBin = (name, realm, authTypes, reqAttr) => ({ type: 'WCPM_ADD_BIN', bin: newBin(name, realm, authTypes, reqAttr)})

export const removeBin = (binNumber) => ({ type: 'WCPM_REMOVE_BIN', binNumber: binNumber })

export const removeContextPath = (binNumber, pathNumber) => ({ type: 'WCPM_REMOVE_PATH', binNumber: binNumber, pathNumber: pathNumber })

