package io.zeebe.extension.awseventbridge.onboarding.worker;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.eventbridge.AmazonEventBridgeAsync;
import com.amazonaws.services.eventbridge.AmazonEventBridgeAsyncClientBuilder;
import com.amazonaws.services.eventbridge.model.CreatePartnerEventSourceRequest;
import com.amazonaws.services.eventbridge.model.CreatePartnerEventSourceResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zeebe.extension.awseventbridge.AwsEventBridgeHelper;
import io.zeebe.extension.awseventbridge.Constants;
import io.zeebe.extension.awseventbridge.data.BridgeConfig;
import io.zeebe.extension.awseventbridge.data.BridgeConfigRepository;
import io.zeebe.extension.awseventbridge.worker.TaskWorker;

@Component
public class CreatePartnerEventSourceDelegate implements JavaDelegate {

  private final Logger logger = LoggerFactory.getLogger(TaskWorker.class);
  
  @Autowired
  private AwsEventBridgeHelper ebHelper;
  
  @Autowired
  private BridgeConfigRepository repo;
  
  @Autowired
  private ObjectMapper mapper;

  @Override
  @Transactional
  public void execute(DelegateExecution execution) throws Exception {

    Long configId = (Long) execution.getVariable(Constants.ONBOARDING_VAR_bridgeConfigEntity);
    BridgeConfig bridgeConfig = repo.findById(configId).get();
    
    AmazonEventBridgeAsync eventBridgeClient = ebHelper.getClient(bridgeConfig.getAwsRegion());
          
    CreatePartnerEventSourceRequest request = new CreatePartnerEventSourceRequest();
    request.setAccount(bridgeConfig.getAwsAccountNumber());
    request.setName( ebHelper.getSourceUrl(bridgeConfig) );
    CreatePartnerEventSourceResult response = eventBridgeClient.createPartnerEventSource(request );
    
    logger.info("Created partner event source. Request: " + request + " | Response: " + response);
   
    execution.setVariable(
        Constants.ONBOARDING_VAR_partnerEventSourceResponse, 
        mapper.writeValueAsString(response));
    
  }


}
