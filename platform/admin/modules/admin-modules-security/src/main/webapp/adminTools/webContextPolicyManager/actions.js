const newBin = (name, realm, authTypes, reqAttr) => ({
  name: name,
  realm: realm,
  authTypes: authTypes,
  reqAttr: reqAttr,
  contextPaths: []
})

export const addBin = (name, realm, authTypes, reqAttr) => ({ type: 'WCPM_ADD_BIN', bin: newBin(name, realm, authTypes, reqAttr)})

export const removeBin = (binNumber) => ({ type: 'WCPM_REMOVE_BIN', binNumber: binNumber })

export const removeContextPath = (binNumber, pathNumber) => ({ type: 'WCPM_REMOVE_PATH', binNumber, pathNumber })

export const addContextPath = (binNumber, path) => ({ type: 'WCPM_ADD_PATH', binNumber, path })

export const editNewContextPath = (binNumber, value) => ({ type: 'WCPM_EDIT_CONTEXT_PATH', binNumber, value})

export const addNewBin = () => ({ type: 'WCPM_ADD_EMPTY_BIN' })

