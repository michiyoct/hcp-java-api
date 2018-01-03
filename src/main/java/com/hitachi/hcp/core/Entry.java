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
 * Represents an Entry in either an object's version history or a directory listing.
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2018-01-03
 */
public class Entry {
  protected String urlName = null; // required
  protected String utf8Name = null; // required
  protected String type = null; // required object or directory
  protected Long size = null;
  protected String hashScheme = null;
  protected String hash = null;
  protected Long retention = null;
  protected String retentionString = null;
  protected String retentionClass = null;
  protected Long ingestTime = null;
  protected String ingestTimeString = null;
  protected Boolean hold = null;
  protected Boolean shred = null;
  protected String dpl = null;
  protected Boolean indexed = null;
  protected Boolean customMetadata = null;
  protected String version = null;
  protected String state = null; // required created or deleted or ...

  // additional fields found through trial and error that are not in the REST Guide:-
  protected String etag = null;
  protected Long versionCreateTime = null;
  protected Boolean customMetadataAnnotations = null;
  protected Boolean replicated = null;
  protected Long changeTime = null;
  protected String changeTimeString = null;
  protected String owner = null;
  protected String domain = null;
  protected Boolean hasAcl = null;

  public Entry() {
    super();
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
  public String getDpl() {
    return dpl;
  }

  /**
   * @param dpl the dpl to set
   */
  public void setDpl(String dpl) {
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

  /**
   * @return the hold
   */
  public Boolean getHold() {
    return hold;
  }

  /**
   * @param hold the hold to set
   */
  public void setHold(Boolean hold) {
    this.hold = hold;
  }

  /**
   * @return the indexed
   */
  public Boolean getIndexed() {
    return indexed;
  }

  /**
   * @param indexed the indexed to set
   */
  public void setIndexed(Boolean indexed) {
    this.indexed = indexed;
  }

  /**
   * @return the ingestTime
   */
  public Long getIngestTime() {
    return ingestTime;
  }

  /**
   * @param ingestTime the ingestTime to set
   */
  public void setIngestTime(Long ingestTime) {
    this.ingestTime = ingestTime;
  }

  /**
   * @return the ingestTimeString
   */
  public String getIngestTimeString() {
    return ingestTimeString;
  }

  /**
   * @param ingestTimeString the ingestTimeString to set
   */
  public void setIngestTimeString(String ingestTimeString) {
    this.ingestTimeString = ingestTimeString;
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
   * @param shred the shread to set
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
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(String state) {
    this.state = state;
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
   * @return the urlName
   */
  public String getUrlName() {
    return urlName;
  }

  /**
   * @param urlName the urlName to set
   */
  public void setUrlName(String urlName) {
    this.urlName = urlName;
  }

  /**
   * @return the utf8Name
   */
  public String getUtf8Name() {
    return utf8Name;
  }

  /**
   * @param utf8Name the utf8Name to set
   */
  public void setUtf8Name(String utf8Name) {
    this.utf8Name = utf8Name;
  }

  /**
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * @param version the version to set
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * @return the etag
   */
  public String getEtag() {
    return etag;
  }

  /**
   * @param etag the etag to set
   */
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * @return the versionCreateTime
   */
  public Long getVersionCreateTime() {
    return versionCreateTime;
  }

  /**
   * @param versionCreateTime the versionCreateTime to set
   */
  public void setVersionCreateTime(Long versionCreateTime) {
    this.versionCreateTime = versionCreateTime;
  }

  /**
   * @return the customMetadataAnnotations
   */
  public Boolean getCustomMetadataAnnotations() {
    return customMetadataAnnotations;
  }

  /**
   * @param customMetadataAnnotations the customMetadataAnnotations to set
   */
  public void setCustomMetadataAnnotations(Boolean customMetadataAnnotations) {
    this.customMetadataAnnotations = customMetadataAnnotations;
  }

  /**
   * @return the replicated
   */
  public Boolean getReplicated() {
    return replicated;
  }

  /**
   * @param replicated the replicated to set
   */
  public void setReplicated(Boolean replicated) {
    this.replicated = replicated;
  }

  /**
   * @return the changeTime
   */
  public Long getChangeTime() {
    return changeTime;
  }

  /**
   * @param changeTime the changeTime to set
   */
  public void setChangeTime(Long changeTime) {
    this.changeTime = changeTime;
  }

  /**
   * @return the changeTimeString
   */
  public String getChangeTimeString() {
    return changeTimeString;
  }

  /**
   * @param changeTimeString the changeTimeString to set
   */
  public void setChangeTimeString(String changeTimeString) {
    this.changeTimeString = changeTimeString;
  }

  /**
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }

  /**
   * @param owner the owner to set
   */
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @param domain the domain to set
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return the hasAcl
   */
  public Boolean getHasAcl() {
    return hasAcl;
  }

  /**
   * @param hasAcl the hasAcl to set
   */
  public void setHasAcl(Boolean hasAcl) {
    this.hasAcl = hasAcl;
  }
}