export default {
  'actions': [
    {
      'method': 'POST',
      'label': 'continue',
      'url': '/security/wizard/bind'
    }
  ],
  'form': {
    'title': 'LDAP Network Settings',
    'questions': [
      {
        'type': 'HOSTNAME',
        'label': 'LDAP Host name',
        'id': 'hostName'
      },
      {
        'type': 'PORT',
        'label': 'LDAP Port',
        'id': 'port',
        'defaults': [
          389,
          636
        ]
      },
      {
        'defaults': [
          'No encryption',
          'Use ldaps',
          'Use startTLS'
        ],
        'id': 'ldapEncryptionMethod',
        'label': 'Encryption method',
        'type': 'STRING_ENUM'
      }
    ]
  },
  'state': {
    'chris': 'isDumb'
  }
}
