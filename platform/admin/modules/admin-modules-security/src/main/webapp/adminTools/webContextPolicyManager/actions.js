// Bin level
export const removeBin = (binNumber) => ({ type: 'WCPM_REMOVE_BIN', binNumber })
export const addNewBin = () => ({ type: 'WCPM_ADD_BIN' })
export const editModeOn = (binNumber) => ({ type: 'WCPM_EDIT_MODE_ON', binNumber })
export const editModeCancel = (binNumber) => ({ type: 'WCPM_EDIT_MODE_CANCEL', binNumber })
export const editModeSave = (binNumber) => ({ type: 'WCPM_EDIT_MODE_SAVE', binNumber })

// Name
export const editName = (binNumber, value) => ({ type: 'WCPM_EDIT_NAME', binNumber, value })

// Realm
export const editRealm = (binNumber, value) => ({ type: 'WCPM_EDIT_REALM', binNumber, value })

// Attribute Lists
export const removeAttribute = (attribute) => (binNumber, pathNumber) => ({ type: 'WCPM_REMOVE_ATTRIBUTE_LIST', attribute, binNumber, pathNumber })
export const addAttribute = (attribute) => (binNumber, path) => ({ type: 'WCPM_ADD_ATTRIBUTE_LIST', attribute, binNumber, path })
export const editAttribute = (attribute) => (binNumber, value) => ({ type: 'WCPM_EDIT_ATTRIBUTE_LIST', attribute, binNumber, value })




