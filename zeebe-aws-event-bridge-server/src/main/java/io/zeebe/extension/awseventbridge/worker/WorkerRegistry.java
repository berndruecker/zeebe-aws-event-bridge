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
  
  private List<TaskWorker> workers = new ArrayList<TaskWorker>();
  
  @PostConstruct
  public void startExistingWorkersOnStartup() {    
    Iterable<BridgeConfig> bridgeList = repo.findAll();

    logger.info("Starting Workers: " + bridgeList);
    
    for (BridgeConfig bridgeConfig : bridgeList) {
      try {
        startWorker(bridgeConfig);
      } catch (Exception ex) {
        logger.error("Could not start worker " + bridgeConfig);
        ex.printStackTrace();
      }
    }
  }
  
  public void startNewWorkerOnDemand(long eventBridgeId) {
    BridgeConfig bridgeEntity = repo.findById(eventBridgeId);
    startWorker(bridgeEntity);
  }

  private void startWorker(BridgeConfig bridgeEntity) {
    TaskWorker taskWorker = new TaskWorker(bridgeEntity, ebHelper, mapper);
    workers.add(taskWorker);
    taskWorker.start();
    logger.info("Started worker: " + bridgeEntity);
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

}
