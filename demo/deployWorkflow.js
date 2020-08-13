'use strict'

const ZB = require('zeebe-node')
const fs = require('fs')

void (async () => {
  const zbc = new ZB.ZBClient()

  const res = await zbc.deployWorkflow('./demo.bpmn')
  console.log(res)
})()
