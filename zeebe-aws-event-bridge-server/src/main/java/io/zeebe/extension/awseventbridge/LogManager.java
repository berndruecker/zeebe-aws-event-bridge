package io.zeebe.extension.awseventbridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.extension.awseventbridge.data.BridgeConfig;
import io.zeebe.extension.awseventbridge.data.BridgeConfigLog;
import io.zeebe.extension.awseventbridge.data.BridgeConfigLogRepository;

@Component
public class LogManager {

  private final Logger logger = LoggerFactory.getLogger(LogManager.class);
  
  @Autowired
  private BridgeConfigLogRepository repo;

  public void log(BridgeConfig config, String message) {    
    logger.info(message);    
    repo.save(new BridgeConfigLog() //
        .setZeebeClusterId(config.getZeebeClusterId()) //
        .setAwsAccountNumber(config.getAwsAccountNumber()) //
        .setEntry(message));
  }
}
