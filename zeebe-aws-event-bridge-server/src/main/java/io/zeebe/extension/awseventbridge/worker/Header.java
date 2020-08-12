package io.zeebe.extension.awseventbridge.worker;

public class Header {

  private String requestCorrelationId;
  private String callbackEndpoint;
  private String camundaCloudClusterId;
  private long camundaCloudJobKey;
  
  private String camundaCloudClientId;
  private String camundaCloudClientSecret;
  
  public String getRequestCorrelationId() {
    return requestCorrelationId;
  }
  public Header setRequestCorrelationId(String requestCorrelationId) {
    this.requestCorrelationId = requestCorrelationId;
    return this;
  }
  public String getCallbackEndpoint() {
    return callbackEndpoint;
  }
  public Header setCallbackEndpoint(String callbackEndpoint) {
    this.callbackEndpoint = callbackEndpoint;
    return this;
  }
  public String getCamundaCloudClusterId() {
    return camundaCloudClusterId;
  }
  public Header setCamundaCloudClusterId(String camundaCloudClusterId) {
    this.camundaCloudClusterId = camundaCloudClusterId;
    return this;
  }
  public long getCamundaCloudJobKey() {
    return camundaCloudJobKey;
  }
  public Header setCamundaCloudJobKey(long camundaCloudJobKey) {
    this.camundaCloudJobKey = camundaCloudJobKey;
    return this;
  }
  public String getCamundaCloudClientId() {
    return camundaCloudClientId;
  }
  public Header setCamundaCloudClientId(String camundaCloudClientId) {
    this.camundaCloudClientId = camundaCloudClientId;
    return this;
  }
  public String getCamundaCloudClientSecret() {
    return camundaCloudClientSecret;
  }
  public Header setCamundaCloudClientSecret(String camundaCloudClientSecret) {
    this.camundaCloudClientSecret = camundaCloudClientSecret;
    return this;
  }

  
}
