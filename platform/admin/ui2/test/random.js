
export const action = (label, method, url) => ({
  label: label || 'check',
  method: method || 'POST',
  url: url || '/admin/security/wizard/network'
})

export const question = (id, label, type, value) => ({
  id: id || 0,
  label: label || 'label',
  type: type || 'HOSTNAME',
  value: value || 'value'
})

export const stage = (actions, title, questions) => ({
  actions: actions || [action()],
  form: {
    title: title || 'title',
    questions: questions || [question()]
  }
})
