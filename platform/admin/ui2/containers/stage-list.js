import React from 'react'
import { connect } from 'react-redux'
import { list } from '../actions'
import Mount from '../components/mount'
import { getList } from '../reducer'

import Paper from 'material-ui/Paper'
import { List, ListItem } from 'material-ui/List'

import { Link } from 'react-router'

const format = (name) => name.split('-')
  .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')

const StageList = ({ list, onList }) => (
  <Mount on={onList}>
    <Paper style={{ margin: 20 }}>
      <List>
        {list.map((stageName, i) =>
          <Link key={i} to={'/stage/' + stageName}>
            <ListItem>{format(stageName)}</ListItem>
          </Link>)}
      </List>
    </Paper>
  </Mount>
)

const mapStateToProps = (state) => ({ list: getList(state) })

export default connect(mapStateToProps, { onList: list })(StageList)
