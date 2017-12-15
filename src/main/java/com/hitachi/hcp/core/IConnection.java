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
package com.hitachi.hcp.core;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Acts as a high level, protocol independent, definition of a Connection object
 * 
 * The underlying implementation could use HTTP, H3, or any other HCP supported protocol in future
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2017-12-15
 */
public interface IConnection {
  // CREATE OBJECT
  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The String textual content of the file to create
   * @return The response wrapper object
   */
  public CreateResponse createObject(String objectPath, String fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The File which holds the content of the file to create
   * @return The response wrapper object
   */
  public CreateResponse createObject(String objectPath, File fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The InputStream containing the content of the file to create
   * @return The response wrapper object
   */
  public CreateResponse createObject(String objectPath, InputStream fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The Serializable Java object representing content of the file to create
   * @return The response wrapper object
   */
  public CreateResponse createObject(String objectPath, Serializable fileContent) throws HCPException;

  // TODO READ OBJECT

  // TODO DELETE OBJECT
}