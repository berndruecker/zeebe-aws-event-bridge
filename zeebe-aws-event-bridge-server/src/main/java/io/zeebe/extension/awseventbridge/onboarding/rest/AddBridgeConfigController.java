package io.zeebe.extension.awseventbridge.onboarding.rest;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.extension.awseventbridge.Constants;
import io.zeebe.extension.awseventbridge.data.BridgeConfig;
import io.zeebe.extension.awseventbridge.data.BridgeConfigRepository;
import io.zeebe.extension.awseventbridge.worker.TaskWorker;

@RestController
public class AddBridgeConfigController {

  private final Logger logger = LoggerFactory.getLogger(TaskWorker.class);

  @Autowired
  private BridgeConfigRepository repo;

  @Autowired
  private ProcessEngine engine;

  @RequestMapping(path = "/BridgeConfig", method = PUT)
  public void createPartnerEventSource(BridgeConfig config) {

    repo.save(config);

    ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceByKey( //
        Constants.PROCESS_KEY_ONBOARDING, Variables.createVariables().putValue(Constants.ONBOARDING_VAR_bridgeConfigEntity, config.getId()));

    logger.debug("Started onboarding process " + processInstance + " for " + config);
  }

}