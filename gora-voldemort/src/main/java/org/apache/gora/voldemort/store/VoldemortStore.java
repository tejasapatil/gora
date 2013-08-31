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
import java.util.List;
import org.apache.gora.persistency.impl.PersistentBase;
import org.apache.gora.query.PartitionQuery;
import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.impl.DataStoreBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class VoldemortStore<K,T extends PersistentBase> extends DataStoreBase<K,T> {
  
  protected static final String INSTANCE_NAME_PROPERTY = "voldemort.instance";
  protected static final String SERVER_URL = "voldemort.zookeepers";
  protected static final String DEFAULT_MAPPING_FILE = "gora-voldemort-mapping.xml";

  public static final Logger LOG = LoggerFactory.getLogger(VoldemortStore.class);

@Override
public String getSchemaName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void createSchema() {
	// TODO Auto-generated method stub
	
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

@Override
public T get(K key, String[] fields) {
	// TODO Auto-generated method stub
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
	// TODO Auto-generated method stub
	
}
  
}
