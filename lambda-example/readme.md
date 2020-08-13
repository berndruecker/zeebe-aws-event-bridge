## conventions

- this lambda is expected to get invoked via AWS partner event bridge with a very specific event payload (see test.js for an example).
- message correlation will be based on the message name `ACK` and the correlation key expression `=requestCorrelationId`

## known issues

- your lambda might not work once deployed to AWS if it uses native dependencies that were compiled for another target environment. To fix this, you can make use of this docker command to rebuild your node_modules for the AWS archictcture:

  `docker run --rm -v "$PWD":/var/task lambci/lambda:build-nodejs12.x npm rebuild grpc --build-from-source`
