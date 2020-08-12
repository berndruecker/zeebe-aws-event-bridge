package io.zeebe.extension.awseventbridge.onboarding.rest;


import javax.transaction.Transactional;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import io.zeebe.extension.awseventbridge.Constants;
import io.zeebe.extension.awseventbridge.data.BridgeConfig;
import io.zeebe.extension.awseventbridge.data.BridgeConfigRepository;
import io.zeebe.extension.awseventbridge.worker.TaskWorker;

@Controller
public class AddBridgeConfigController {
  
  private final Logger logger = LoggerFactory.getLogger(TaskWorker.class);

  @Autowired
  private BridgeConfigRepository repo;
  
  @Autowired
  private ProcessEngine engine;

  @PostMapping("/addBridgeConfig")
  public String createPartnerEventSource(BridgeConfig config) {
    
    repo.save(config);
    
    ProcessInstance processInstance = engine.getRuntimeService().startProcessInstanceByKey( //
        Constants.ONBOARDING_PROCESS_KEY, 
        Variables.createVariables().putValue(Constants.ONBOARDING_VAR_bridgeConfigEntity, config.getId()));
        
    logger.debug("Started onboarding process " + processInstance + " for " + config);

    return "showMessage";
  }

}