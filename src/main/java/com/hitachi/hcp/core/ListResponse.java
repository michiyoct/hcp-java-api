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

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a list of entries from a directory or version history from the server
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 * @since 1.0 2018-01-03
 */
public class ListResponse extends BaseResponse {
  protected ArrayList<Entry> list = new ArrayList<Entry>();

  protected String path = null;
  protected String utf8Path = null;
  protected String parentDir = null;
  protected Boolean dirDeleted = null;
  protected Boolean showDeleted = null;

  /**
   * Creates a ListResponse
   */
  public ListResponse() {
    super();
  }

  public ListResponse(Status status, String explanation) {
    super(status, explanation);
  }

  /**
   * @return the dirDeleted
   */
  public Boolean getDirDeleted() {
    return dirDeleted;
  }

  /**
   * @param dirDeleted the dirDeleted to set
   */
  public void setDirDeleted(Boolean dirDeleted) {
    this.dirDeleted = dirDeleted;
  }

  /**
   * @return the list
   */
  public List<Entry> getList() {
    return list;
  }

  /**
   * Adds an Entry item to the list
   * @param entry The Entry item to add
   */
  public void add(Entry entry) {
    list.add(entry);
  }

  /**
   * @return the parentDir
   */
  public String getParentDir() {
    return parentDir;
  }

  /**
   * @param parentDir the parentDir to set
   */
  public void setParentDir(String parentDir) {
    this.parentDir = parentDir;
  }

  /**
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return the showDeleted
   */
  public Boolean getShowDeleted() {
    return showDeleted;
  }

  /**
   * @param showDeleted the showDeleted to set
   */
  public void setShowDeleted(Boolean showDeleted) {
    this.showDeleted = showDeleted;
  }

  /**
   * @return the utf8Path
   */
  public String getUtf8Path() {
    return utf8Path;
  }

  /**
   * @param utf8Path the utf8Path to set
   */
  public void setUtf8Path(String utf8Path) {
    this.utf8Path = utf8Path;
  }
}