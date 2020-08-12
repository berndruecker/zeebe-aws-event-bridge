package io.zeebe.extension.awseventbridge.data;

import org.springframework.data.repository.CrudRepository;

public interface BridgeConfigRepository extends CrudRepository<BridgeConfig, Long> {

//  List<BridgeConfig> findByLastName(String lastName);

  BridgeConfig findById(long id);
}
