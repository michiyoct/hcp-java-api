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
 * Represents the delete response from the server for ease of use in applications.
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 * @since 1.0 2018-01-03
 */
public class DeleteResponse extends BaseResponse {
  
  /**
   * Creates a DeleteResponse
   */
  public DeleteResponse() {
    super();
  }

  public DeleteResponse(Status status,String explanation) {
    super(status, explanation);
  }

}