'use strict'

const { ZBClient, Duration } = require('zeebe-node')

;(async () => {
  const zbc = new ZBClient()
  const result = await zbc.createWorkflowInstance('test', {
    testData: 'something'
  })
  console.log(result)
})()
