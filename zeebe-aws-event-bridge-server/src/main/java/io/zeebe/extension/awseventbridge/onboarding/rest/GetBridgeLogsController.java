package io.zeebe.extension.awseventbridge.onboarding.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.extension.awseventbridge.data.BridgeConfigLog;
import io.zeebe.extension.awseventbridge.data.BridgeConfigLogRepository;

@RestController
public class GetBridgeLogsController {

  @Autowired
  private BridgeConfigLogRepository repo;

  @RequestMapping(path = "/BridgeConfig/{awsAccountNumber}/{zeebeClusterId}/logs", method = GET)
  public Iterator<BridgeConfigLog> createPartnerEventSource(@PathVariable("awsAccountNumber") String awsAccountNumber, @PathVariable("zeebeClusterId") String zeebeClusterId) {

    Iterator<BridgeConfigLog> logs = repo.findByZeebeClusterIdAndAwsAccountNumber(zeebeClusterId, awsAccountNumber).iterator();
    return logs;
  }

}