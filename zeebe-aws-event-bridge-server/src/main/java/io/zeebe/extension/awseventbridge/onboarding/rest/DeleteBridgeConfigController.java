package io.zeebe.extension.awseventbridge.onboarding.rest;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import java.util.Iterator;
import java.util.List;

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
public class DeleteBridgeConfigController {

  private final Logger logger = LoggerFactory.getLogger(TaskWorker.class);

  @Autowired
  private BridgeConfigRepository repo;

  @Autowired
  private ProcessEngine engine;

  @RequestMapping(path = "/BridgeConfig", method = DELETE)
  public void createPartnerEventSource(BridgeConfig params) {

    Iterator<BridgeConfig> configList = repo.findByZeebeClusterIdAndAwsAccountNumber(params.getZeebeClusterId(), params.getAwsAccountNumber()).iterator();
    if (!configList.hasNext()) {
      throw new IllegalStateException("No bridge config found");
    }
    BridgeConfig config = configList.next();
    ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceByKey( //
        Constants.PROCESS_KEY_REMOVE, Variables.createVariables().putValue(Constants.ONBOARDING_VAR_bridgeConfigEntity, config.getId()));      
    logger.debug("Started deletion process " + processInstance + " for " + config);      
    
    while (configList.hasNext()) {
      BridgeConfig anotherConfig = configList.next();
      repo.delete(anotherConfig);
      logger.debug("Deleted duplcate entity " + anotherConfig);            
    }

  }

}