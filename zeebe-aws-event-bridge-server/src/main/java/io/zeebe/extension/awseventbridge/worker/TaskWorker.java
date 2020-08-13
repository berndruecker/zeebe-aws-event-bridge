package io.zeebe.extension.awseventbridge.worker;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.eventbridge.AmazonEventBridgeAsync;
import com.amazonaws.services.eventbridge.model.PutPartnerEventsRequest;
import com.amazonaws.services.eventbridge.model.PutPartnerEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutPartnerEventsResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import io.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import io.zeebe.extension.awseventbridge.AwsEventBridgeHelper;
import io.zeebe.extension.awseventbridge.Constants;
import io.zeebe.extension.awseventbridge.LogManager;
import io.zeebe.extension.awseventbridge.data.BridgeConfig;

public class TaskWorker implements JobHandler {

  private AwsEventBridgeHelper ebHelper;
  private ObjectMapper mapper;
  private LogManager logManager;

  private BridgeConfig bridgeConfig;
  private ZeebeClient client;
  private JobWorker worker;

  public TaskWorker(BridgeConfig bridgeConfig, AwsEventBridgeHelper ebHelper, ObjectMapper mapper, LogManager logManager) {
    this.bridgeConfig = bridgeConfig;
    this.ebHelper = ebHelper;
    this.mapper = mapper;
    this.logManager = logManager;
  }

  public void start() {
    final OAuthCredentialsProvider cred = new OAuthCredentialsProviderBuilder() //
        .audience(bridgeConfig.getZeebeAudience()) //
        .clientId(bridgeConfig.getZeebeClientId()) //
        .clientSecret(bridgeConfig.getZeebeClientSecret()) //
        .build();

    client = ZeebeClient.newClientBuilder() //
        .brokerContactPoint(bridgeConfig.getZeebeBrokerEndpoint()) //
        .credentialsProvider(cred) //
        .build();

    worker = client.newWorker() //
        .jobType(Constants.JOB_TYPE) //
        .handler(this) //
        .name("AwsEventBridgeWorker") //
        .open();
  }

  public void stop() {
    worker.close();
    client.close();
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {
    logManager.log(bridgeConfig, "Handling ServiceTask for AWS event bridge: " + job);

    AmazonEventBridgeAsync ebClient = ebHelper.getClient(bridgeConfig.getAwsRegion());

    String requestCorrelationId = UUID.randomUUID().toString();

    Details details = new Details();

    details.getHeader() //
        .setCallbackEndpoint(bridgeConfig.getZeebeBrokerEndpoint()) //
        .setCamundaCloudClusterId(bridgeConfig.getZeebeClusterId()) //
        .setCamundaCloudJobKey(job.getKey()) //
        .setRequestCorrelationId(requestCorrelationId);
    if (bridgeConfig.isRelayZeebeClientCredentials()) {
      details.getHeader() //
          .setCamundaCloudClientId(bridgeConfig.getZeebeClientId()).setCamundaCloudClientSecret(bridgeConfig.getZeebeClientSecret());
    }

    details.setPayload(job.getVariables());

    String detailsJson = mapper.writeValueAsString(details);

    PutPartnerEventsRequest request = new PutPartnerEventsRequest();
    PutPartnerEventsRequestEntry entry = new PutPartnerEventsRequestEntry();
    entry.setDetail(detailsJson);
    entry.setDetailType("TODO");
    entry.setSource(ebHelper.getSourceUrl(bridgeConfig));
    entry.setTime(new Date());

    request.setEntries(Collections.singletonList(entry));
    PutPartnerEventsResult response = ebClient.putPartnerEvents(request);

    logManager.log(bridgeConfig, "Sent event to AWS Event Bridge. Request: " + request + " | response : " + response);

    HashMap<String, Object> variables = new HashMap<String, Object>();
    // TODO: Name of variable??
    variables.put("requestCorrelationId", requestCorrelationId);
    client.newCompleteCommand(job.getKey()).variables(variables).send().join();
  }

  public BridgeConfig getBridgeConfig() {
    return bridgeConfig;
  }

}
