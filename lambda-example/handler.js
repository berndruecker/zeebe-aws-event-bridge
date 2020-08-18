'use strict'

const zbHelper = require('./zeebeHelper') //camunda-cloud-zeebe-aws-eventbridge-helper
const axios = require('axios')

module.exports.main = async event => {
  //do fancy business logic...
  let jokeOfTheDay = 'no joke for you today...'
  try {
    const jokeResponse = await axios.get(
      'https://sv443.net/jokeapi/v2/joke/Programming?type=twopart'
    )

    jokeOfTheDay = jokeResponse.data.setup + ' ' + jokeResponse.data.delivery
  } catch (e) {
    console.log(e)
  }

  //tell zeebe that we're done:
  await zbHelper.confirmTaskCompletion(event, { jokeOfTheDay })

  return {
    jokeOfTheDay,
  }
}
