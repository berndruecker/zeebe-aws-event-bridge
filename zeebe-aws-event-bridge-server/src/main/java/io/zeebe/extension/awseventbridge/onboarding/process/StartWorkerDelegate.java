package io.zeebe.extension.awseventbridge.onboarding.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.extension.awseventbridge.Constants;
import io.zeebe.extension.awseventbridge.worker.WorkerRegistry;

@Component
public class StartWorkerDelegate implements JavaDelegate {
  
  @Autowired
  private WorkerRegistry registry;

  @Override
  public void execute(DelegateExecution execution) throws Exception {

    registry.startNewWorkerOnDemand((Long) execution.getVariable(Constants.ONBOARDING_VAR_bridgeConfigEntity));
    
  }

}
