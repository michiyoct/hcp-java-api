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
 * Represents a HCP Exception from the server. Wraps a message and a root cause stack track, if available.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2017-12-08
 */
public class HCPException extends Exception {
  static final long serialVersionUID = 1;

  /**
   * Creates a HCPException instance
   * 
   * @param message the String message of the problem
   * @param parent the underlying Java exception causing the issue (if a client issue), or null (if a server issue).
   */
  public HCPException(String message,Throwable parent) {
    super(message,parent);
  }

}