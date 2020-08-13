## How to run the end-to-end demo

### steps to be taken by Camunda

1. start the zeebe-aws-event-bridge-server

   ```bash
   docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID={CAMUNDA-PARTNER-ACCOUNT-AWS-ACCESS-KEY-ID} -e AWS_SECRET_ACCESS_KEY={CAMUNDA-PARTNER-ACCOUNT-AWS-AWS-SECRET-ACCESSKEY} berndruecker/zeebe-aws-event-bridge-server
   ```

### steps to be taken by 'customer'

1. create a zeebe cluster and configure a new client at
   https://console.cloud.camunda.io/. You'll need the client credentials in the
   next step.

1. set up environment variables

   ```bash
   export ZEEBE_ADDRESS="{CLUSTER-ID}.zeebe.camunda.io:443"
   export ZEEBE_CLIENT_ID="{CLIENT-ID}"
   export ZEEBE_CLIENT_SECRET="{CLIENT-SECRET}"
   export ZEEBE_AUTHORIZATION_SERVER_URL="https://login.cloud.camunda.io/oauth/token"
   ```

1. deploy the demo workflow to Camunda Cloud

   ```bash
   node deployWorkflow.js
   ```

1. build and deploy the example lambda function

   ```bash
   cd ../lambda-example/libs/nodejs
   npm install
   cd ../..
   npm install
   serverless deploy
   ```

1. trigger the creation of a custom partner event bus

   - go to http://localhost:8080/index.html > `new event bridge`
   - fill out the form with the client credentials obtained above from Camunda
     Cloud
   - use your own AWS account ID for the field "Account Number"
   - click `add bridge config`. This will create a new partner event bus
     [in your AWS account](https://eu-central-1.console.aws.amazon.com/events/home?region=eu-central-1#/eventbuses)
     that looks something like
     `aws.partner/camunda.com.test/ebtask/cb07421e-54a5-44a6-7542-b502ef088e42`

1. configure your lambda to be triggered via EventBridge

   - in your AWS console navigate to `Amazon EventBridge > Events > Rules`
     [(link)](https://eu-central-1.console.aws.amazon.com/events/home?region=eu-central-1#/rules)
   - click on `create rule`
   - in the `Name and description` section
     - enter a rule name, e.g. `zeebe-cloud-task`
   - in the `Define Pattern` section
     - select `event pattern` and then `custom pattern`
     - in the field `Pre-defined event pattern` paste the following JSON
       ```json
       {
         "account": ["{YOUR-AWS-ACCOUNT-ID}"]
       }
       ```
     - click `save` to save the pattern
   - in the `Select event bus` section
     - select `Custom or partner event bus`
     - select the new event bus (prefixed with `aws.partner/camunda.com`)
   - in the `Select targets` section
     - select `Lambda function` as a target
     - select the function you deployed earlier
   - click on `create` at the bottom of the page

1. start a new process instance

   ```bash
   cd ../demo
   node startProcess.js
   ```

1. Verify that the process instance was indeed created via the Camunda Cloud
   Operate app
   - if the process was started successfully (but not yet completed by your
     lambda), you should see a new running instance
   - if your lambda competed successfully, Operate should display the process
     instance as `finished`
