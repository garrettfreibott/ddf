import React from 'react'

import styles from './home.less'

const Input = ({ type }) => {
  switch (type) {
    case 'String':
      return <input className={styles.input} type='text' />
    default:
      return <span>Unknown input type {JSON.stringify(type)}.</span>
  }
}

const Option = ({ label, type }) => {
  return (
    <div>
      <div>{label}</div>
      <Input type={type} />
    </div>
  )
}

const Content = ({ title, options = [] }) => {
  return (
    <div>
      <h2>{title}</h2>
      <div>{options.map((option, i) => <Option key={i} {...option}/>)}</div>
    </div>
  )
}

const Section = ({ contents = [] }) => {
  return (
    <div>
      {contents.map((content, i) => <Content key={i} {...content} />)}
    </div>
  )
}

export default ({ wizards }) => {
  const sections = wizards.sections || []
  return (
    <div className={styles.home}>
      <b>Admin UI 2</b>
      <h1>{wizards.title}</h1>
      {sections.map((section, i) => <Section key={i} {...section} />)}
      <pre>{JSON.stringify(wizards, null, 2).slice(0,0)}</pre>
    </div>
  );
}
