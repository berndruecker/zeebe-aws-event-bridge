package io.zeebe.extension.awseventbridge.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BridgeConfigLogRepository extends CrudRepository<BridgeConfigLog, Long> {

  List<BridgeConfigLog> findByZeebeClusterIdAndAwsAccountNumber(String zeebeClusterId, String awsAccountNumber);

  BridgeConfig findById(long id);
}
