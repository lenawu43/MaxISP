'use strict'
const merge = require('webpack-merge')
// const prodEnv = require('./prod.env')
var sitEnv = require('./sit.env')

module.exports = merge(sitEnv, {
  NODE_ENV: '"development"'
})
