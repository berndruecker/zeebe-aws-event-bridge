package io.zeebe.extension.awseventbridge.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BridgeConfigRepository extends CrudRepository<BridgeConfig, Long> {

  List<BridgeConfig> findByZeebeClusterIdAndAwsAccountNumber(String zeebeClusterId, String awsAccountNumber);

  BridgeConfig findById(long id);
}
