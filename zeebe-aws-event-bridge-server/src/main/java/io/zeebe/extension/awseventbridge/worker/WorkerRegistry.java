package io.zeebe.extension.awseventbridge.worker;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.zeebe.extension.awseventbridge.AwsEventBridgeHelper;
import io.zeebe.extension.awseventbridge.LogManager;
import io.zeebe.extension.awseventbridge.data.BridgeConfig;
import io.zeebe.extension.awseventbridge.data.BridgeConfigRepository;

@Component
public class WorkerRegistry {
  
  private final Logger logger = LoggerFactory.getLogger(TaskWorker.class);
  
  @Autowired
  private BridgeConfigRepository repo;
  
  @Autowired
  private AwsEventBridgeHelper ebHelper;

  @Autowired
  private ObjectMapper mapper;
  
  @Autowired
  private LogManager logManager;

  private List<TaskWorker> workers = new ArrayList<TaskWorker>();
  
  @PostConstruct
  public void startExistingWorkersOnStartup() {    
    Iterable<BridgeConfig> bridgeList = repo.findAll();

    logger.info("Starting Workers: " + bridgeList);
    
    for (BridgeConfig bridgeConfig : bridgeList) {
      if (bridgeConfig.isActive()) {
        try {
          startWorker(bridgeConfig);
        } catch (Exception ex) {
          logger.error("Could not start worker " + bridgeConfig, ex);
        }
      }
    }
  }
  
  public void startNewWorkerOnDemand(long bridgeConfigId) {
    BridgeConfig bridgeConfig = repo.findById(bridgeConfigId);
    startWorker(bridgeConfig);
  }

  private void startWorker(BridgeConfig bridgeConfig) {
    TaskWorker taskWorker = new TaskWorker(bridgeConfig, ebHelper, mapper, logManager);
    workers.add(taskWorker);
    taskWorker.start();
    logManager.log(bridgeConfig, "Started worker: " + bridgeConfig);
  }

  @PreDestroy
  public void stopWorkersOnShutdown() {
    for (TaskWorker worker : workers) {
      try {
        worker.stop();
      } catch (Exception ex) {
        logger.error("Could not stop worker correctly " + worker);
        ex.printStackTrace();
      }
    }
  }
  
  private TaskWorker findWorker(long bridgeConfigId) {
    for (TaskWorker worker : workers) {
      if (worker.getBridgeConfig().getId() == bridgeConfigId) {
        return worker;
      }
    }
    return null;
  }

  public void stopAndRetireWorker(Long bridgeConfigId) {
    TaskWorker worker = findWorker(bridgeConfigId);
    worker.stop();
    
    BridgeConfig bridgeConfig = repo.findById(bridgeConfigId).get();
    bridgeConfig.setActive(false);
  }

}
