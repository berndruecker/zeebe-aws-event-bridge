// {
//   "version": "0",
//   "id": "59e0537c-ba23-f89e-0f52-b0f580896237",
//   "detail-type": "TODO",
//   "source": "aws.partner/camunda.com.test/ebtask/cb07420e-54a5-44a6-9542-b502ff088e32",
//   "account": "146271617960",
//   "time": "2020-08-12T14:58:55Z",
//   "region": "eu-central-1",
//   "resources": [],
//   "detail": {
//       "header": {
//           "requestCorrelationId": "e8d1d97d-faaa-4e7c-85fe-82d448023597",
//           "callbackEndpoint": "cb07420e-54a5-44a6-9542-b502ff088e32.zeebe.camunda.io:443",
//           "camundaCloudClusterId": "cb07420e-54a5-44a6-9542-b502ff088e32",
//           "camundaCloudJobKey": 2251799813686373,
//           "camundaCloudClientId": "nVY67jJQX4kLR5p3nC9EqkKQlmjUAEgu",
//           "camundaCloudClientSecret": "wAE_WMqayxK1zVHJqBoipsnleuFHU2c7oDg.-dprnSp.y_r7wEH9vh5DmwSPFz7q"
//       },
//       "payload": {}
//   }
// }

'use strict'

const { ZBClient, Duration } = require('zeebe-node')

const clients = {}

const getOrCreateZbClient = function ({ clientId, clientSecret, clusterId }) {
  clientId = clientId || 'default'

  if (!clients[clientId]) {
    if (clientId === 'default') {
      console.log('creating new default ZBClient')
      clients[clientId] = new ZBClient() // tries to use environment variables
    } else {
      console.log(`creating new ZBClient ${clientId}`)
      clients[clientId] = new ZBClient({
        camundaCloud: {
          clientId,
          clientSecret,
          clusterId,
        },
      })
    }
  }

  return clients[clientId]
}

module.exports.confirmTaskCompletion = function (
  AwsEventBridgeEvent,
  variables = {}
) {
  const { header } = AwsEventBridgeEvent.detail

  const clusterId = header.camundaCloudClusterId || undefined
  const clientId = header.camundaCloudClientId || undefined
  const clientSecret = header.camundaCloudClientSecret || undefined
  const correlationKey = header.requestCorrelationId

  const zbc = getOrCreateZbClient({ clientId, clientSecret, clusterId })

  zbc.publishMessage({
    correlationKey,
    messageId: AwsEventBridgeEvent.id,
    name: 'ACK',
    variables,
    timeToLive: Duration.seconds.of(30), // seconds
  })
}
