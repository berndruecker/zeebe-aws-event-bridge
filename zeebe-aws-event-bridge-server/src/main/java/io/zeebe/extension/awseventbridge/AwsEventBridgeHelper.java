package io.zeebe.extension.awseventbridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.eventbridge.AmazonEventBridgeAsync;
import com.amazonaws.services.eventbridge.AmazonEventBridgeAsyncClientBuilder;

import io.zeebe.extension.awseventbridge.data.BridgeConfig;

@Component
public class AwsEventBridgeHelper {

  @Value("${aws.access_key_id}")
  private String aws_access_key_id = "Your AWS ID goes here";

  @Value("${aws.secret_access_key}")
  private String aws_secret_access_key = "Your AWS secret key goes here";

  public AmazonEventBridgeAsync getClient(String region) throws Exception {
    BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(aws_access_key_id, aws_secret_access_key);
    AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAwsCredentials);

    AmazonEventBridgeAsyncClientBuilder builder = AmazonEventBridgeAsyncClientBuilder.standard();
    builder.setCredentials(awsStaticCredentialsProvider);
    builder.setRegion(region);

    AmazonEventBridgeAsync eventBridgeClient = builder.build();
    return eventBridgeClient;
  }

  public String getSourceUrl(BridgeConfig bridgeConfig) {
    return "aws.partner/camunda.com.test/ebtask/" + bridgeConfig.getZeebeClusterId();
  }

}
