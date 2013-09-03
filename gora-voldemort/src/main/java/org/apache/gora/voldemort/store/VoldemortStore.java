/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gora.voldemort.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.gora.persistency.impl.PersistentBase;
import org.apache.gora.query.PartitionQuery;
import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.DataStoreFactory;
import org.apache.gora.store.impl.DataStoreBase;
import org.apache.gora.voldemort.encoders.Encoder;
import org.apache.gora.voldemort.store.VoldemortMapping.VoldemortMappingBuilder;
import org.apache.gora.voldemort.encoders.BinaryEncoder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

public class VoldemortStore<K,T extends PersistentBase> extends DataStoreBase<K,T> {

  public static final String DEFAULT_MAPPING_FILE = "gora-oracle-mapping.xml";
  public static final Logger LOG = LoggerFactory.getLogger(VoldemortStore.class);
  
  private volatile VoldemortMapping mapping;
  private Encoder encoder;
  private StoreClient<Class<K>, byte[]> client = null;

  // Set of operations to be executed during flush() 
  // LinkedHashSet<List<Operation>> operations;

  @Override
  public void initialize(Class<K> keyClass, Class<T> persistentClass,
      Properties properties) {
    super.initialize(keyClass, persistentClass, properties);

    if (mapping != null && client != null && encoder != null) {
      LOG.warn("VoldemortStore is already initialised");
      return;
    }
    
    try {
      String mappingFile = DataStoreFactory.getMappingFile(properties, this, DEFAULT_MAPPING_FILE);
      LOG.debug("mappingFile = " + mappingFile);
      mapping = readMapping(mappingFile);
    }
    catch (IOException e) {
      LOG.error(e.getMessage());
      LOG.error(e.getStackTrace().toString());
    }

    // setup a voldemort client for the given store
    StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(mapping.getServerUrl()));
    client = factory.getStoreClient(mapping.getStore());
    
    encoder = new BinaryEncoder();
  }

  /**
   * Reads the schema file and converts it into a data structure to be used
   * @param mappingFilename The schema file to be mapped into a table
   * @return VoldemortMapping Object containing all necessary information to create tables
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private VoldemortMapping readMapping(String mappingFilename) throws IOException {

    VoldemortMappingBuilder mappingBuilder = new VoldemortMapping.VoldemortMappingBuilder();

    try {
      SAXBuilder builder = new SAXBuilder();
      LOG.debug("Parsing " + mappingFilename);
      InputStream mappingFile = getClass().getClassLoader().getResourceAsStream(mappingFilename);

      if (mappingFile == null)
        throw new IOException("Unable to create input stream for file " + mappingFilename);

      Document doc = builder.build(mappingFile);
      List<Element> classes = (List<Element>) doc.getRootElement().getChildren("class");

      for (Element classElement : classes) {
        if (classElement.getAttributeValue("keyClass").equals(keyClass.getCanonicalName()) && 
            classElement.getAttributeValue("name").equals(persistentClass.getCanonicalName())) {

          mappingBuilder.setStore(classElement.getAttributeValue("store"));
          mappingBuilder.setServerUrl(classElement.getAttributeValue("serverurl"));          
          mappingBuilder.setKeyClass(classElement.getAttributeValue("keyClass"));
          mappingBuilder.setKey(classElement.getChild("key").getAttributeValue("name"));

          List<Element> fields = classElement.getChildren("field");
          for (Element field : fields)
            mappingBuilder.addField(field.getAttributeValue("name"));

          break;
        }
      }
      LOG.debug("Parsing of " + mappingFilename + " finished");
      return mappingBuilder.build();
    }
    catch (Exception ex) {
      LOG.error("Error while parsing " + mappingFilename + " : " + ex.getMessage());
      throw new IOException(ex);
    }
  }

  /**
   * Gets the schema name. In voldemort world, store corresponds to the schema
   * @return String The schema name
   */
  @Override
  public String getSchemaName() {
    return mapping.getStore();
  }

  /**
   * Voldemort auto-creates stores if they are not present, so do nothing here
   */
  @Override
  public void createSchema() {
  }

  @Override
  public void deleteSchema() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean schemaExists() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Returns the object corresponding to the given key
   * @param key the key of the object
   * @param fields the fields required in the object. Pass null, to retrieve all fields
   * @return the Persistent object with the specified fields
   */
  @Override
  public T get(K key, String[] fields) {
    if (key == null)
      return null;
    return null;
  }

  @Override
  public void put(K key, T obj) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean delete(K key) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public long deleteByQuery(Query<K, T> query) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Result<K, T> execute(Query<K, T> query) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Query<K, T> newQuery() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PartitionQuery<K, T>> getPartitions(Query<K, T> query)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flush() {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() {
    LOG.debug("close");
    flush();
  }
}
