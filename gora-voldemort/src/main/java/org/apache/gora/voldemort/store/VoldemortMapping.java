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

import java.util.LinkedList;
import java.util.List;

import org.apache.gora.util.GoraException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoldemortMapping {
  private String store = null;
  private String serverUrl = null;
  private String key = null;
  private String keyClass = null;
  private List<String> fields = null;

  public VoldemortMapping() {
    fields = new LinkedList<String>();
  }
  
  public VoldemortMapping(String store, String serverUrl, String key, String keyClass, List<String> fields) {
    this.store = store;
    this.serverUrl = serverUrl;
    this.key = key;
    this.keyClass= keyClass; 
    this.fields = fields;
  }
  
  public String getStore() {
    return store;
  }
  public void setStore(String store) {
    this.store = store;
  }
  public String getServerUrl() {
    return serverUrl;
  }
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public String getKeyClass() {
    return keyClass;
  }
  public void setKeyClass(String keyClass) {
    this.keyClass = keyClass;
  }
  public List<String> getFields() {
    return fields;
  }
  public void setFields(List<String> fields) {
    this.fields = fields;
  }  

  public static class VoldemortMappingBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(VoldemortMappingBuilder.class);

    private String store = null;
    private String serverUrl = null;
    private String key = null;
    private String keyClass = null;
    private List<String> fields = null;

    public void setStore(String store) {
      this.store = store;
    }
    public void setServerUrl(String serverUrl) {
      this.serverUrl = serverUrl;
    }
    public void setKey(String key) {
      this.key = key;
    }
    public void setKeyClass(String keyClass) {
      this.keyClass = keyClass;
    }
    public void addField(String field) {
      fields.add(field);
    }

    /**
     * Verifies that all properties are valid and constructs the VoldemortMapping object.
     * @return A newly constructed mapping
     * @throws GoraException
     */
    public VoldemortMapping build() throws GoraException {
      if (store == null)      throw new GoraException("store is not specified.");
      if (serverUrl == null)  throw new GoraException("serverUrl is not specified.");
      if (key == null)        throw new GoraException("key is not specified.");
      if (keyClass == null)   throw new GoraException("keyClass is not specified.");
      if (fields.isEmpty())   throw new GoraException("No fields specified.");
      LOG.debug("VoldemortMappingBuilder.build completed all checks.");

      return new VoldemortMapping(store, serverUrl, key, keyClass, fields);
    }
  }
}
