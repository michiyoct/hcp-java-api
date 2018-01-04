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
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 * @since 1.0 2017-12-15
 */
public interface IConnection {
  // CREATE OBJECT
  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The String textual content of the file to create
   * @return The response wrapper object
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public CreateResponse createObject(String objectPath, String fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The File which holds the content of the file to create
   * @return The response wrapper object
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public CreateResponse createObject(String objectPath, File fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The InputStream containing the content of the file to create
   * @return The response wrapper object
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public CreateResponse createObject(String objectPath, InputStream fileContent) throws HCPException;

  /**
   * Creates an object using String content
   * 
   * @param objectPath The path on HCP of the object to create
   * @param fileContent The Serializable Java object representing content of the file to create
   * @return The response wrapper object
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public CreateResponse createObject(String objectPath, Serializable fileContent) throws HCPException;

  // READ OBJECT
  /**
   * Reads an object from HCP
   * 
   * @param objectPath The path on HCP of the object to read
   * @return The response wrapper object, which contains a method to retrieve the InputStream
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public ReadResponse readObject(String objectPath) throws HCPException;

  // TODO read just a segment of the file given a byte offset

  /**
   * Reads just the metadata of an object, not its content
   * 
   * @param objectPath The path on HCP of the object to read
   * @return The response wrapper object, which contains methods to retrieve the object's metadata
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public ReadMetadataResponse readObjectMetadata(String objectPath) throws HCPException;

  // TODO DELETE OBJECT

  /**
   * Deletes the specified object from HCP
   * 
   * @param objectPath The path of the object to delete
   * @return The deletion response object with the results of this call
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public DeleteResponse deleteObject(String objectPath) throws HCPException;

  // LIST OBJECTS

  /**
   * Lists all HCP objects within a folder.
   * 
   * @param folderPath The folder to list objects within
   * @return A ListResponse object containing the response results, and a list of entries from the folder
   * @throws HCPException If any problems occur with the underlying connection mechanism during the call.
   */
  public ListResponse listObjects(String folderPath) throws HCPException;

  // TODO fetch HCP namespace information (statistics XML element, HCP Guide page 55, section 4.5.3)

  // TODO fetch object version lists

  // TODO fetch object versions

  // TODO fetch custom metadata from objects

  // TODO fetch namespace lists, statistics, permissions, and retention classes
}