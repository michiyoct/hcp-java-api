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

/**
 * Represents the response from the server for ease of use in applications.
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2017-12-15
 */
public class CreateResponse {
  /**
   * Represents the HCP response status.
   */
  public enum Status {
    CREATED, CONFLICT, FORBIDDEN, TOO_LARGE, OTHER_FAILURE
  }

  private boolean success = true;

  private Status status;

  private String explanation = null;

  private String location = null;
  private String hash = null;
  private long clusterTime = -1L;

  private String versionId = null;

  /**
   * Creates a CreateResponse indicating failure
   * 
   * @param status The Status of the HCP Response
   * @param explanation The explanation of the failure
   */
  public CreateResponse(Status status,String explanation) {
    this.status = status;
    this.explanation = explanation;
    this.success = false;
  }

  /**
   * Creates a CreateResponse indicating success
   * 
   * @param status A CREATED status success indicator
   * @param location The file's location on the HCP Server
   * @param hash The SHA-256 hash value for the file
   * @param clusterTime The cluster creation time for the file
   */
  public CreateResponse(Status status,String location,String hash,long clusterTime,String versionId) {
    this.status = status;
    this.location = location;
    this.hash = hash;
    this.clusterTime = clusterTime;
    this.success = true;
    this.versionId = versionId;
  }

  /**
   * Returns whether the creation command was successful or not
   * @return If we were successful
   */
  public boolean isSuccessful() {
    return success;
  }

  /**
   * Returns the Status of the response
   * 
   * @see Status
   * @return The Status of the response
   */
  public Status getStatus() {
    return status;
  }

  /**
   * For a failure, shows the explanation of the failure. null for success.
   * @return The String explanation message for the failure
   */
  public String getExplanation() {
    return explanation;
  }

  /**
   * Returns the file path that was created
   * @return String the file path on HCP
   */
  public String getLocation() {
    return location;
  }

  /**
   * Returns the SHA-256 hash value for the file that has been created
   * @return The String value of the SHA-256 hash
   */
  public String getHash() {
    return hash;
  }

  /**
   * Returns the HCP cluster time at which the file was created
   * @return The epoch time in millis on the cluster at which the file was created
   */
  public long getClusterTime() {
    return clusterTime;
  }

  /**
   * Returns the Version ID in HCP of the new object version, if versioning enabled.
   * @return The version ID in HCP, or null if versioning not enabled
   */
  public String getVersionId() {
    return versionId;
  }
}