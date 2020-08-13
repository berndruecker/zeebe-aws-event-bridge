package io.zeebe.extension.awseventbridge.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BridgeConfig {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;

  private String zeebeClusterId;
  private String zeebeClientId;
  private String zeebeClientSecret;
  
  private boolean relayZeebeClientCredentials;
  
  private String awsAccountNumber;
  private String awsRegion;
  
  /**
   * Per default a config is activle, but during deletion it might be inactive
   */
  private boolean active = true;
  
  public String getZeebeAudience() {
    return zeebeClusterId + ".zeebe.camunda.io";    
  }
  public String getZeebeBrokerEndpoint() {
    return getZeebeAudience() + ":443";
  }

  public String getZeebeClusterId() {
    return zeebeClusterId;
  }
  public void setZeebeClusterId(String zeebeClusterId) {
    this.zeebeClusterId = zeebeClusterId;
  }
  public String getZeebeClientId() {
    return zeebeClientId;
  }
  public void setZeebeClientId(String zeebeClientId) {
    this.zeebeClientId = zeebeClientId;
  }
  public String getZeebeClientSecret() {
    return zeebeClientSecret;
  }
  public void setZeebeClientSecret(String zeebeClientSecret) {
    this.zeebeClientSecret = zeebeClientSecret;
  }
  public String getAwsAccountNumber() {
    return awsAccountNumber;
  }
  public void setAwsAccountNumber(String awsAccountNumber) {
    this.awsAccountNumber = awsAccountNumber;
  }
  @Override
  public String toString() {
    return "ZeebeAwsEventBridgeEntity [id=" + id + ", zeebeClusterId=" + zeebeClusterId + ", zeebeClientId=" + zeebeClientId + ", zeebeClientSecret="
        + zeebeClientSecret + ", awsAccountNumber=" + awsAccountNumber + "]";
  }
  public String getAwsRegion() {
    return awsRegion;
  }
  public void setAwsRegion(String awsRegion) {
    this.awsRegion = awsRegion;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public boolean isRelayZeebeClientCredentials() {
    return relayZeebeClientCredentials;
  }
  public void setRelayZeebeClientCredentials(boolean relayZeebeClientCredentials) {
    this.relayZeebeClientCredentials = relayZeebeClientCredentials;
  }
  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }
  
}
