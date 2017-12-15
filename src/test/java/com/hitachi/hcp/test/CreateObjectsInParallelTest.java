/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package com.hitachi.hcp.test;

import com.hitachi.hcp.test.util.CreateObjectThread;
import com.hitachi.hcp.test.util.ConnectionManager;

import com.hitachi.hcp.core.IConnection;
import com.hitachi.hcp.core.HCPException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for using multiple HCP connections in parallel
 */
public class CreateObjectsInParallelTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public CreateObjectsInParallelTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(CreateObjectsInParallelTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testCreateObjectsInParallel() throws HCPException {
    int perThread = 100;
    int threads = 10;
    for (int i = 0;i < threads;i++) {
      IConnection conn = ConnectionManager.getConnection(); // one per thread for our testing purposes
      CreateObjectThread thread = new CreateObjectThread(conn,i * perThread,((i + 1)*perThread)-1,"Wibble");
      thread.run();
    }

    // TODO wait for threads
    // TODO ensure each completed successfully

    assertTrue(true);
  }
}
