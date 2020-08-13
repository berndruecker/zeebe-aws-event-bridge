'use strict'

const { ZBClient, Duration } = require('zeebe-node')

;(async () => {
  const zbc = new ZBClient()
  const result = await zbc.createWorkflowInstance('demo', {
    testData: 'something',
    myCurrentTime: new Date().toISOString()
  })
  console.log(result)
})()
