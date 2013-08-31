/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.gora.store;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.gora.GoraTestDriver;
import org.apache.gora.examples.generated.Employee;
import org.apache.gora.examples.generated.Metadata;
import org.apache.gora.examples.generated.WebPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A base class for {@link DataStore} tests. This is just a convenience
 * class, which actually only uses {@link DataStoreTestUtil} methods to
 * run the tests. Not all test cases can extend this class (like TestHBaseStore),
 * so all test logic should reside in DataStoreTestUtil class.
 */
public abstract class DataStoreTestBase {

  public static final Logger log = LoggerFactory.getLogger(DataStoreTestBase.class);

  protected static GoraTestDriver testDriver;

  protected DataStore<String,Employee> employeeStore;
  protected DataStore<String,WebPage> webPageStore;

  @Deprecated
  protected abstract DataStore<String,Employee> createEmployeeDataStore() throws IOException;

  @Deprecated
  protected abstract DataStore<String,WebPage> createWebPageDataStore() throws IOException;


  /** junit annoyingly forces BeforeClass to be static, so this method
   * should be called from a static block
   */
  protected static void setTestDriver(GoraTestDriver driver) {
    testDriver = driver;
  }

  private static boolean setUpClassCalled = false;

  @BeforeClass
  public static void setUpClass() throws Exception {
    if(testDriver != null && !setUpClassCalled) {
      log.info("setting up class");
      testDriver.setUpClass();
      setUpClassCalled = true;
    }
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    if(testDriver != null) {
      log.info("tearing down class");
      testDriver.tearDownClass();
    }
  }

  @Before
  public void setUp() throws Exception {
    //There is an issue in JUnit 4 tests in Eclipse where TestSqlStore static
    //methods are not called BEFORE setUpClass. I think this is a bug in
    //JUnitRunner in Eclipse. Below is a workaround for that problem.
    if(!setUpClassCalled) {
      setUpClass();
    }

    log.info("setting up test");
    if(testDriver != null) {
      employeeStore = testDriver.createDataStore(String.class, Employee.class);
      webPageStore = testDriver.createDataStore(String.class, WebPage.class);
      testDriver.setUp();
    } else {
      employeeStore =  createEmployeeDataStore();
      webPageStore = createWebPageDataStore();

      employeeStore.truncateSchema();
      webPageStore.truncateSchema();
    }
  }

  @After
  public void tearDown() throws Exception {
    log.info("tearing down test");
    if(testDriver != null) {
      testDriver.tearDown();
    }
    //employeeStore.close();
    //webPageStore.close();
  }

  @Test
  public void testNewInstance() throws IOException, Exception {
    log.info("test method: testNewInstance");
    DataStoreTestUtil.testNewPersistent(employeeStore);
  }

  @Test
  public void testCreateSchema() throws Exception {
    log.info("test method: testCreateSchema");
    DataStoreTestUtil.testCreateEmployeeSchema(employeeStore);
    assertSchemaExists("Employee");
  }


  // Override this to assert that schema is created correctly

  public void assertSchemaExists(String schemaName) throws Exception {
  }

  @Test
  public void testAutoCreateSchema() throws Exception {
    log.info("test method: testAutoCreateSchema");
    DataStoreTestUtil.testAutoCreateSchema(employeeStore);
    assertAutoCreateSchema();
  }

  public void assertAutoCreateSchema() throws Exception {
    assertSchemaExists("Employee");
  }

  @Test
  public  void testTruncateSchema() throws Exception {
    log.info("test method: testTruncateSchema");
    DataStoreTestUtil.testTruncateSchema(webPageStore);
    assertSchemaExists("WebPage");
  }

  @Test
  public void testDeleteSchema() throws IOException, Exception {
    log.info("test method: testDeleteSchema");
    DataStoreTestUtil.testDeleteSchema(webPageStore);
  }

  @Test
  public void testSchemaExists() throws Exception {
    log.info("test method: testSchemaExists");
    DataStoreTestUtil.testSchemaExists(webPageStore);
  }

  @Test
  public void testPut() throws IOException, Exception {
    log.info("test method: testPut");
    Employee employee = DataStoreTestUtil.testPutEmployee(employeeStore);
    assertPut(employee);
  }

  public void assertPut(Employee employee) throws IOException {
  }

  @Test
  public void testPutNested() throws IOException, Exception {
    log.info("test method: testPutNested");
    DataStoreTestUtil.testPutNested(webPageStore);
  }

  @Test
  public void testPutArray() throws IOException, Exception {
    log.info("test method: testPutArray");
    DataStoreTestUtil.testPutArray(webPageStore);
    assertPutArray();
  }

  public void assertPutArray() throws IOException {
  }

  @Test
  public void testPutBytes() throws IOException, Exception {
    log.info("test method: testPutBytes");
    byte[] contentBytes = DataStoreTestUtil.testPutBytes(webPageStore);
    assertPutBytes(contentBytes);
  }

  public void assertPutBytes(byte[] contentBytes) throws IOException {
  }

  @Test
  public void testPutMap() throws IOException, Exception {
    log.info("test method: testPutMap");
    DataStoreTestUtil.testPutMap(webPageStore);
    assertPutMap();
  }

  public void assertPutMap() throws IOException {
  }

  @Test
  public void testUpdate() throws IOException, Exception {
    log.info("test method: testUpdate");
    DataStoreTestUtil.testUpdateEmployee(employeeStore);
    DataStoreTestUtil.testUpdateWebPage(webPageStore);
  }

  public void testEmptyUpdate() throws IOException, Exception {
    DataStoreTestUtil.testEmptyUpdateEmployee(employeeStore);
  }

  @Test
  public void testGet() throws IOException, Exception {
    log.info("test method: testGet");
    DataStoreTestUtil.testGetEmployee(employeeStore);
  }

  @Test
  /**
   * Tests put and get a record with a nested recursive record
   * Employee with a boss (nested).
   * @throws IOException
   * @throws Exception
   */
  public void testGetRecursive() throws IOException, Exception {
    log.info("test method: testGetRecursive") ;
    DataStoreTestUtil.testGetEmployeeRecursive(employeeStore) ;
  }

  @Test
  /**
   * Tests put and get a record with a double  nested recursive record
   * Employee with a boss (nested).
   * @throws IOException
   * @throws Exception
   */
  public void testGetDoubleRecursive() throws IOException, Exception {
    log.info("test method: testGetDoubleRecursive") ;
    DataStoreTestUtil.testGetEmployeeDoubleRecursive(employeeStore) ;
  }

  @Test
  /**
   * Tests put and get a record with a nested record (not recursive)
   * The webpage of an Employee
   * @throws IOException
   * @throws Exception
   */
  public void testGetNested() throws IOException, Exception {
    log.info("test method: testGetNested") ;
    DataStoreTestUtil.testGetEmployeeNested(employeeStore) ;
  }

  @Test
  /**
   * Tests put and get a record with a 3 types union, and
   * having the value of the 3rd type.
   * @throws IOException
   * @throws Exception
   */
  public void testGet3UnionField() throws IOException, Exception {
    log.info("test method: testGet3UnionField") ;
    DataStoreTestUtil.testGetEmployee3UnionField(employeeStore) ;
  }

  @Test
  public void testGetWithFields() throws IOException, Exception {
    log.info("test method: testGetWithFields");
    DataStoreTestUtil.testGetEmployeeWithFields(employeeStore);
  }

  @Test
  public void testGetWebPage() throws IOException, Exception {
    log.info("test method: testGetWebPage");
    DataStoreTestUtil.testGetWebPage(webPageStore);
  }

  @Test
  public void testGetWebPageDefaultFields() throws IOException, Exception {
    log.info("test method: testGetWebPageDefaultFields");
    DataStoreTestUtil.testGetWebPageDefaultFields(webPageStore);
  }

  @Test
  public void testGetNonExisting() throws Exception, Exception {
    log.info("test method: testGetNonExisting");
    DataStoreTestUtil.testGetEmployeeNonExisting(employeeStore);
  }

 @Test
  public void testQuery() throws IOException, Exception {
    log.info("test method: testQuery");
    DataStoreTestUtil.testQueryWebPages(webPageStore);
  }

  @Test
  public void testQueryStartKey() throws IOException, Exception {
    log.info("test method: testQueryStartKey");
    DataStoreTestUtil.testQueryWebPageStartKey(webPageStore);
  }

  @Test
  public void testQueryEndKey() throws IOException, Exception {
    log.info("test method: testQueryEndKey");
    DataStoreTestUtil.testQueryWebPageEndKey(webPageStore);
  }

  @Test
  public void testQueryKeyRange() throws IOException, Exception {
    log.info("test method: testQueryKetRange");
    DataStoreTestUtil.testQueryWebPageKeyRange(webPageStore);
  }

 @Test
  public void testQueryWebPageSingleKey() throws IOException, Exception {
   log.info("test method: testQueryWebPageSingleKey");
    DataStoreTestUtil.testQueryWebPageSingleKey(webPageStore);
  }

  @Test
  public void testQueryWebPageSingleKeyDefaultFields() throws IOException, Exception {
    log.info("test method: testQuerySingleKeyDefaultFields");
    DataStoreTestUtil.testQueryWebPageSingleKeyDefaultFields(webPageStore);
  }

  @Test
  public void testQueryWebPageQueryEmptyResults() throws IOException, Exception {
    log.info("test method: testQueryEmptyResults");
    DataStoreTestUtil.testQueryWebPageEmptyResults(webPageStore);
  }

  @Test
  public void testDelete() throws IOException, Exception {
    log.info("test method: testDelete");
    DataStoreTestUtil.testDelete(webPageStore);
  }

  @Test
  public void testDeleteByQuery() throws IOException, Exception {
    log.info("test method: testDeleteByQuery");
    DataStoreTestUtil.testDeleteByQuery(webPageStore);
  }

  @Test
  public void testDeleteByQueryFields() throws IOException, Exception {
    log.info("test method: testQueryByQueryFields");
    DataStoreTestUtil.testDeleteByQueryFields(webPageStore);
  }

  @Test
  public void testGetPartitions() throws IOException, Exception {
    log.info("test method: testGetPartitions");
    DataStoreTestUtil.testGetPartitions(webPageStore);
  }
}
