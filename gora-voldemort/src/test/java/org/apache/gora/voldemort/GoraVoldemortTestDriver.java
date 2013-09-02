package org.apache.gora.voldemort;

import org.apache.commons.io.FileUtils;
import org.apache.gora.GoraTestDriver;
import org.apache.gora.voldemort.store.VoldemortStore;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.server.VoldemortConfig;
import voldemort.server.VoldemortServer;
import voldemort.utils.Props;
import voldemort.versioning.Versioned;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoraVoldemortTestDriver extends GoraTestDriver {
  private static VoldemortServer server = null;
  private static StoreClient<String, String> client = null;
  private static String TEST_CLIENT = "test";
  private static Logger log = LoggerFactory.getLogger(GoraVoldemortTestDriver.class);

  /**
   * Constructor
   */
  public GoraVoldemortTestDriver() {
    super(VoldemortStore.class);
  }
  
  @Override
  public void setUpClass() throws Exception {
    super.setUpClass();
    log.info("Initializing Voldemort");
    startVoldemortServer();
    createClient();
  }

  @Override
  public void tearDownClass() throws Exception {
    super.tearDownClass();
    server.stop();
    cleanupData();
    log.info("Terminated voldemort server");
  }

  private static void startVoldemortServer() throws IOException {		
    Props properties = new Props();
    properties.put("node.id", 0);
    properties.put("max.threads", 50);
    properties.put("http.enable", "true");
    properties.put("socket.enable", "true");        
    properties.put("bdb.write.transactions", "false");
    properties.put("bdb.flush.transactions", "false");
    properties.put("bdb.cache.size", "500mb");
    properties.put("enable.nio.connector", "true");
    properties.put("request.format", "vp3");
    properties.put("storage.configs", "voldemort.store.bdb.BdbStorageConfiguration, voldemort.store.readonly.ReadOnlyStorageConfiguration");
    properties.put("voldemort.home", "target/test-classes/single_node_cluster");
    properties.put("metadata.directory", "target/test-classes/single_node_cluster/config");

    try {
      printClasspath();
      VoldemortConfig config = new VoldemortConfig(properties);
      System.out.println("Starting voldemort server...");
      server = new VoldemortServer(config);
    } 
    catch (Exception e) {
     e.printStackTrace();
    }
    

    log.debug("Starting voldemort server...");
    if(!server.isStarted())
      server.start();

    log.info("Voldemort server started");
    Runtime.getRuntime().addShutdownHook(new Thread() {	  // add a shutdown hook to stop the server
    @Override
      public void run() {
        if(server.isStarted()) {
          server.stop();
          try {
	          cleanupData();
          } catch (Exception e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
          }
        }
      }
    });

    System.out.println("Voldemort server started");
  }
  
  private static void setVoldemortClient() {
    String bootstrapUrl = "tcp://localhost:6666";
    StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
    StoreClient<String, String> client = factory.getStoreClient("test");
  }

  private void createClient() throws IOException {
    String bootstrapUrl = "tcp://localhost:6666";
    StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));
    client = factory.getStoreClient(TEST_CLIENT);
  }

  public Versioned<String> get(String key){
    return client.get(key);	
  }

  private static void deleteIfExists(File someFile) throws Exception {
    if (someFile.exists()) {
      if(someFile.isFile())
        FileUtils.forceDelete(someFile);
      else
        FileUtils.deleteDirectory(someFile);
    }
  }
	
  private static void cleanupData() throws Exception {
    File file = new File("src/test/resources/single_node_cluster/data");
    deleteIfExists(file);
    file = new File("src/test/resources/single_node_cluster/config/voldsys$_metadata_version_persistence.version");
    deleteIfExists(file);		
    file = new File("src/test/resources/single_node_cluster/config/voldsys$_metadata_version_persistence");
    deleteIfExists(file);
    file = new File("src/test/resources/single_node_cluster/config/.temp");
    deleteIfExists(file);
    file = new File("src/test/resources/single_node_cluster/config/.version");
    deleteIfExists(file);						
  }
}
