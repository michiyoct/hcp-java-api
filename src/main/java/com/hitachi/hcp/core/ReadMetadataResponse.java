/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Pentaho : http://www.pentaho.com
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
 * Represents the metadata read response from the server for ease of use in applications.
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2018-01-03
 */
public class ReadMetadataResponse extends BaseResponse {
  protected String type;
  protected Long size;
  protected String hash;
  protected String hashScheme;
  protected String versionId;
  protected Long ingestTime;
  protected String retentionClass;
  protected String retentionString;
  protected Long retention;
  protected Boolean hold;
  protected Boolean shred;
  protected Long dpl;
  protected Boolean indexed;
  protected Boolean customMetadata;

  public ReadMetadataResponse() {
    super();
  }

  public ReadMetadataResponse(Status status,String explanation) {
    super(status,explanation);
  }

  /**
   * @return the customMetadata
   */
  public Boolean getCustomMetadata() {
    return customMetadata;
  }

  /**
   * @param customMetadata the customMetadata to set
   */
  public void setCustomMetadata(Boolean customMetadata) {
    this.customMetadata = customMetadata;
  }

  /**
   * @return the dpl
   */
  public Long getDpl() {
    return dpl;
  }

  /**
   * @param dpl the dpl to set
   */
  public void setDpl(Long dpl) {
    this.dpl = dpl;
  }

  /**
   * @return the hash
   */
  public String getHash() {
    return hash;
  }

  /**
   * @param hash the hash to set
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * @return the indexed
   */
  public Boolean isIndexed() {
    return indexed;
  }

  /**
   * @param indexed the indexed to set
   */
  public void setIndexed(Boolean indexed) {
    this.indexed = indexed;
  }

  /**
   * @param ingestTime the ingestTime to set
   */
  public void setIngestTime(Long ingestTime) {
    this.ingestTime = ingestTime;
  }

  /**
   * Returns the ingestTime
   */
  public Long getIngestTime() {
    return ingestTime;
  }

  /**
   * @return the indexed
   */
  public Boolean getIndexed() {
    return indexed;
  }

  /**
   * @return the retention
   */
  public Long getRetention() {
    return retention;
  }

  /**
   * @param retention the retention to set
   */
  public void setRetention(Long retention) {
    this.retention = retention;
  }

  /**
   * @return the retentionClass
   */
  public String getRetentionClass() {
    return retentionClass;
  }

  /**
   * @param retentionClass the retentionClass to set
   */
  public void setRetentionClass(String retentionClass) {
    this.retentionClass = retentionClass;
  }

  /**
   * @return the retentionHold
   */
  public Boolean getRetentionHold() {
    return hold;
  }

  /**
   * @param retentionHold the retentionHold to set
   */
  public void setRetentionHold(Boolean retentionHold) {
    this.hold = retentionHold;
  }

  /**
   * @return the retentionString
   */
  public String getRetentionString() {
    return retentionString;
  }

  /**
   * @param retentionString the retentionString to set
   */
  public void setRetentionString(String retentionString) {
    this.retentionString = retentionString;
  }

  /**
   * @return the shred
   */
  public Boolean getShred() {
    return shred;
  }

  /**
   * @param shred the shred to set
   */
  public void setShred(Boolean shred) {
    this.shred = shred;
  }

  /**
   * @return the size
   */
  public Long getSize() {
    return size;
  }

  /**
   * @param size the size to set
   */
  public void setSize(Long size) {
    this.size = size;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the versionId
   */
  public String getVersionId() {
    return versionId;
  }

  /**
   * @param versionId the versionId to set
   */
  public void setVersionId(String versionId) {
    this.versionId = versionId;
  }

  /**
   * @return the hashScheme
   */
  public String getHashScheme() {
    return hashScheme;
  }

  /**
   * @param hashScheme the hashScheme to set
   */
  public void setHashScheme(String hashScheme) {
    this.hashScheme = hashScheme;
  }

}