/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/

module.exports = function (filters, logs) {
  var level = filters.level || 'ALL'
  var fields = Object.keys(filters).filter(function (field) {
    return field !== 'level' && filters[field] !== ''
  })

  return logs.filter(function (entry) {
    return level === 'ALL' || entry.level === level
  }).filter(function (entry) {
    return fields.reduce(function (match, field) {
      if (!entry[field]) {
        return match
      }

      return entry[field].toLowerCase()
        .match(new RegExp(filters[field], 'i')) && match
    }, true)
  })
}
