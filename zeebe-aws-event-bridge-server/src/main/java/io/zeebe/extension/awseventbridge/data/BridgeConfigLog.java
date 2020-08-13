package io.zeebe.extension.awseventbridge.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BridgeConfigLog {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private String awsAccountNumber;
  private String zeebeClusterId;

  private Date timestamp = new Date();
  
  private String entry;

  public String getEntry() {
    return entry;
  }

  public BridgeConfigLog setEntry(String entry) {
    this.entry = entry;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getAwsAccountNumber() {
    return awsAccountNumber;
  }

  public BridgeConfigLog setAwsAccountNumber(String awsAccountNumber) {
    this.awsAccountNumber = awsAccountNumber;
    return this;
  }

  public String getZeebeClusterId() {
    return zeebeClusterId;
  }

  public BridgeConfigLog setZeebeClusterId(String zeebeClusterId) {
    this.zeebeClusterId = zeebeClusterId;
    return this;
  }
}
