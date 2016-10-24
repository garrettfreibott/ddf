export const list = () => new Promise((resolve, reject) => {
  resolve(['ldapNetworkSettingsStage'])
})

export const fetchStage = (id) =>
  window.fetch('/admin/wizard/ldap/' + id, {credentials: 'same-origin'})
    .then((res) => res.json())

export const submit = (stage, { method, url }) =>
  window.fetch(url, { body: JSON.stringify(stage), method, credentials: 'same-origin' })
    .then((res) => Promise.all([ res.status, res.json() ]))

