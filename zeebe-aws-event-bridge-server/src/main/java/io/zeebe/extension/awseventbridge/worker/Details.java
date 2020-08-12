package io.zeebe.extension.awseventbridge.worker;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class Details {

  private Header header = new Header();
  
  @JsonRawValue 
  private String payload;

  public String getPayload() {
    return payload;
  }

  public Details setPayload(String payload) {
    this.payload = payload;
    return this;
  }

  public Header getHeader() {
    return header;
  }
  
}
