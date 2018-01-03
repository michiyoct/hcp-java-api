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

import com.hitachi.hcp.test.util.ConnectionManager;

import com.hitachi.hcp.core.IConnection;
import com.hitachi.hcp.core.ReadResponse;
import com.hitachi.hcp.core.DeleteResponse;
import com.hitachi.hcp.core.ReadMetadataResponse;
import com.hitachi.hcp.core.CreateResponse;
import com.hitachi.hcp.core.HCPException;

import java.io.InputStream;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CreateObjectTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public CreateObjectTest( String testName ) {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite( CreateObjectTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testCreateObject() throws HCPException {
    IConnection conn = ConnectionManager.getConnection();
    CreateResponse resp = conn.createObject( "/CreateObjectTest.txt", "Basic text file content here" );
    boolean success = resp.isSuccess();

    assertTrue( success );
  }

  /**
   * Tests to ensure closing of the socket to the server is independent of reading the object content
   */
  public void testReadObject() throws HCPException, IOException {
    System.out.println("==================================================");
    System.out.println("CreateObjectTest.testReadObject()");
    IConnection conn = ConnectionManager.getConnection();
    ReadResponse resp = conn.readObject( "/CreateObjectTest.txt" );
    InputStream is = resp.getInputStream();
    byte[] buffer = new byte[1];
    int len;
    if ( -1 != ( len = is.read( buffer ) ) ) {
      System.out.print( "Read: " + new String( buffer ) );
    }
    try {
      Thread.sleep( 10000L );
    } catch ( InterruptedException ie ) {
      // expected so do nothing
    }
    while ( -1 != ( len = is.read( buffer ) ) ) {
      System.out.print( new String( buffer ) );
    }
    System.out.println();
    boolean success = resp.isSuccess();

    System.out.println("==================================================");
    assertTrue( success );
  }

  /**
   * Tests to grab an object's metadata and show on screen (no exceptions)
   */
  public void testReadObjectMetadata() throws HCPException, IOException {
    System.out.println("==================================================");
    System.out.println("CreateObjectTest.testReadObjectMetadata()");
    IConnection conn = ConnectionManager.getConnection();
    ReadMetadataResponse resp = conn.readObjectMetadata( "/CreateObjectTest.txt" );

    System.out.println("Req Status:      " + resp.getStatus());
    System.out.println("Req Explanation: " + resp.getExplanation());
    System.out.println("Content type:    " + resp.getContentType());
    System.out.println("Content Length:  " + resp.getContentLength());
    System.out.println("Size:            " + resp.getSize());
    System.out.println("Type:            " + resp.getType());
    System.out.println("Hash:            " + resp.getHash());
    System.out.println("Hash Scheme:     " + resp.getHashScheme());
    System.out.println("Retention Class: " + resp.getRetentionClass());
    System.out.println("Retention Desc:  " + resp.getRetentionString());
    System.out.println("Retention:       " + resp.getRetention());
    System.out.println("Retention Hold:  " + resp.getRetentionHold());
    System.out.println("Version Id:      " + resp.getVersionId());
    System.out.println("DPL:             " + resp.getDpl());
    System.out.println("Ingest Time:     " + resp.getIngestTime());
    System.out.println("Time:            " + resp.getTime());
    System.out.println("Custom Metadata: " + resp.getCustomMetadata());
    System.out.println("Indexed:         " + resp.getIndexed());
    System.out.println("Shred:           " + resp.getShred());

    boolean success = resp.isSuccess();

    System.out.println("==================================================");
    assertTrue( success );
  }

  public void testDeleteObject() throws HCPException {
    System.out.println("==================================================");
    System.out.println("CreateObjectTest.testReadObjectMetadata()");
    IConnection conn = ConnectionManager.getConnection();
    DeleteResponse resp = conn.deleteObject( "/CreateObjectTest.txt" );
    
    boolean success = resp.isSuccess();

    System.out.println("==================================================");
    assertTrue( success );
  }
}