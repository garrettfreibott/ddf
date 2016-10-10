const api = {
  GET: {
    'network-settings': () => ({
      actions: [
        {
          label: "continue",
          method: "POST",
          url: "/network-settings"
        }
      ],
      form: require('./network-settings')
    })
  },
  POST: {
    '/network-settings': (stage) => {
      const {
        hostname,
        port,
        encryptionMethod
      } = stage.form.questions.reduce((o, q, i) => {
        o[q.id] = q.value
        return o
      }, {})

      return {
        state: {
          hostname,
          port,
          encryptionMethod
        },
        actions: [
          {
            label: "continue",
            method: "POST",
            url: "/network-settings"
          }
        ],
        form: require('./bind-settings')
      }
    },
    '/bind-settings': (stage) => {
    }
  }
}

export const list = () => new Promise((resolve, reject) => {
  resolve(Object.keys(api.GET))
})

export const fetch = (id) => new Promise((resolve, reject) => {
  const fn = api.GET[id]
  if (fn) {
    resolve(fn())
  } else {
    reject({ message: 'not found' })
  }
})

export const submit = (stage, { method, url}) => new Promise((resolve, reject) => {
  const fn = api[method][url]
  if (fn) {
    resolve(fn(stage))
  } else {
    reject({ message: 'not found' })
  }
})

