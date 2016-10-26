import React from 'react'
import { connect } from 'react-redux'

import { getBackendErrors } from '../reducer'

import { message, stub } from './exceptions.less'

console.log(stub)

const PrettyStackLine = ({ declaringClass, methodName, fileName, lineNumber }) => (
    <div>
    <a href={'http://localhost:8091?message=' + fileName + ':' + lineNumber}>at {declaringClass}.{methodName}({fileName}:{lineNumber})</a>
    </div>
)

const PrettyStackTrace = ({ lines }) => (
    <div>
        {lines.map((line, i) => <PrettyStackLine key={i} {...line} />)}
    </div>
)

const Exception = ({ cause, stackTrace }) => {
    if (cause == undefined && stackTrace === undefined) return null

    return (
        <div>
            <div className={stub}></div>
            <div className={message}>
                <div>{cause}</div>
                <PrettyStackTrace lines={stackTrace} />
            </div>
        </div>
    )
}

const mapStateToProps = (state) => getBackendErrors(state)

export default connect(mapStateToProps)(Exception)