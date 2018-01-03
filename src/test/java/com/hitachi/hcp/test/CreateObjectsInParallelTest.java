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
import com.hitachi.hcp.core.ListResponse;
import com.hitachi.hcp.core.Entry;
import com.hitachi.hcp.core.HCPException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

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
    int perThread = 10;
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

  public void testListObjects() throws HCPException {
    System.out.println("==================================================");
    System.out.println("CreateObjectsInParallelTest.testListObjects()");
    IConnection conn = ConnectionManager.getConnection();
    ListResponse resp = conn.listObjects( "/batch" ); // deliberately leaving the trailing / off so it is corrected by the API

    // print out minimal system metadata
    System.out.println("HCP Server used: " + resp.getServicedBy());
    System.out.println("HCP Time:        " + resp.getTime());
    System.out.println("HCP Request Id:  " + resp.getRequestId());
    System.out.println("Was success?:    " + resp.isSuccess());
    System.out.println("Status:          " + resp.getStatus());

    System.out.println("Entries:-");
    for (Entry entry: resp.getList()) {
      System.out.println("  " + entry.getUrlName());
    }
    System.out.println("End of entries list");
    
    boolean success = resp.isSuccess();

    System.out.println("==================================================");
    assertTrue( success );
  }
}
