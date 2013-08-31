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

import org.apache.gora.examples.generated.Employee;
import org.apache.gora.examples.generated.WebPage;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.gora.store.DataStoreTestBase;
import org.apache.gora.voldemort.GoraVoldemortTestDriver;
import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Base Gora tests for the VoldemortStore.
 *
 */
public class TestVoldemortStore extends DataStoreTestBase {

  private Configuration conf;

  static {
    setTestDriver(new GoraVoldemortTestDriver());
  }
  
  public GoraVoldemortTestDriver getTestDriver() {
    return (GoraVoldemortTestDriver) testDriver;
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
  }
  @SuppressWarnings("unchecked")
  @Override
  @Deprecated
  protected DataStore<String, Employee> createEmployeeDataStore() throws IOException {
    return DataStoreFactory.getDataStore(VoldemortStore.class, String.class, Employee.class, conf);
  }

  @SuppressWarnings("unchecked")
  @Override
  @Deprecated
  protected DataStore<String, WebPage> createWebPageDataStore() throws IOException {
    return DataStoreFactory.getDataStore(VoldemortStore.class, String.class, WebPage.class, conf);
  }
  
  @Test
  public void testPrint() throws IOException, Exception {
    System.out.println("Running test...");
  }
  @Override
  @Ignore
  @Test
  public void testNewInstance() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testCreateSchema() throws Exception {
  }

  @Override
  @Ignore
  @Test
  public void testAutoCreateSchema() throws Exception {
  }

  @Override
  @Ignore
  @Test
  public  void testTruncateSchema() throws Exception {
  }

  @Override
  @Ignore
  @Test
  public void testDeleteSchema() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testSchemaExists() throws Exception {
  }

  @Override
  @Ignore
  @Test
  public void testPut() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testPutNested() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testPutArray() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testPutBytes() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testPutMap() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testUpdate() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testEmptyUpdate() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGet() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  /**
   * Tests put and get a record with a nested recursive record
   * Employee with a boss (nested).
   * @throws IOException
   * @throws Exception
   */
  public void testGetRecursive() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  /**
   * Tests put and get a record with a double  nested recursive record
   * Employee with a boss (nested).
   * @throws IOException
   * @throws Exception
   */
  public void testGetDoubleRecursive() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  /**
   * Tests put and get a record with a nested record (not recursive)
   * The webpage of an Employee
   * @throws IOException
   * @throws Exception
   */
  public void testGetNested() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  /**
   * Tests put and get a record with a 3 types union, and
   * having the value of the 3rd type.
   * @throws IOException
   * @throws Exception
   */
  public void testGet3UnionField() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGetWithFields() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGetWebPage() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGetWebPageDefaultFields() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGetNonExisting() throws Exception, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQuery() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryStartKey() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryEndKey() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryKeyRange() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryWebPageSingleKey() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryWebPageSingleKeyDefaultFields() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testQueryWebPageQueryEmptyResults() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testDelete() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testDeleteByQuery() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testDeleteByQueryFields() throws IOException, Exception {
  }

  @Override
  @Ignore
  @Test
  public void testGetPartitions() throws IOException, Exception {
  }

}
