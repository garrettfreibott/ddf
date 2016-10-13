const api = {
  GET: {
    'network-settings': () => ({
      actions: [
        {
          label: 'continue',
          method: 'POST',
          url: '/network-settings'
        }
      ],
      form: require('./network-settings')
    }),
    'discover-sources': () => ({
      actions: [
        {
          label: 'discover sources',
          method: 'POST',
          url: '/discover-sources'
        },
        {
          label: 'manually configure',
          method: 'POST',
          url: '/current-add-source'
        }
      ],
      form: {
        title: 'Source Information',
        description: 'Enter information about your source',
        questions: [
          {
            id: 'sourceName',
            label: 'Source Name',
            type: 'STRING'
          },
          {
            'id': 'hostname',
            'label': 'Source Host name',
            'type': 'HOSTNAME'
          },
          {
            'id': 'port',
            'label': 'Source Port',
            'type': 'PORT',
            'value': 8993
          }
        ]
      }
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
            label: 'continue',
            method: 'POST',
            url: '/network-settings'
          }
        ],
        form: require('./bind-settings')
      }
    },
    '/bind-settings': (stage) => {
    },
    '/discover-sources': (stage) => {
      return {
        form: {
          title: 'Results',
          description: '',
          questions: [
            {
              id: 'recommended',
              label: '',
              type: 'STRING_ENUM',
              defaults: [
                'Recommended',
                'Customize'
              ]
            }
          ]
        }
      }
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

export const submit = (stage, { method, url }) => new Promise((resolve, reject) => {
  const fn = api[method][url]
  if (fn) {
    resolve(fn(stage))
  } else {
    reject({ message: 'not found' })
  }
})

