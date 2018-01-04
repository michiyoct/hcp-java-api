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

import java.io.InputStream;

/**
 * This class is the absolute minimum data that is returned from a HCP call
 * 
 * Designed to be protocol (HTTP/H3) independent.
 * 
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 * @since 1.0 2018-01-03
 */
public class BaseResponse {
  protected Long contentLength = 0L;
  protected String contentType = null;
  protected Long time = 0L;
  protected Boolean success = false;
  protected InputStream inputStream = null;
  protected String explanation = null;
  protected String requestId = null;
  protected String servicedBy = null;
  
  /**
   * Represents the HCP response status.
   * 
   * See HCP REST Developer's Guide page 59 for full details
   */
  public enum Status {
    OK, // HTTP 200 - GET,HEAD,POST,DELETE
    CREATED, // HTTP 201 - PUT
    NO_CONTENT, // HTTP 204 - GET, HEAD
    PARTIAL_CONTENT, // HTTP 206 - GET
    AUTH_MISSING, // HTTP 302 - ALL
    BAD_REQUEST, // HTTP 400 - ALL
    UNAUTHORIZED, // HTTP 401 - ALL
    FORBIDDEN, // HTTP 403 - ALL
    NOT_FOUND, // HTTP 404 - DELETE, HEAD, GET, POST, PUT
    CONFLICT, // HTTP 409 - DELETE, PUT
    FILE_TOO_LARGE, // HTTP 413 - PUT
    REQUEST_TOO_LARGE, // HTTP 414 - ALL
    REQUEST_RANGE_INVALID, // HTTP 416 - GET (Range/Partial)
    INTERNAL_ERROR, // HTTP 500 - ALL
    UNAVAILABLE // HTTP 503 - ALL (Can retry at 15, 30, 60 second intervals)
    ,
    OTHER_FAILURE // Any other HTTP response or partial response occurred
  };

  protected Status status;

  public BaseResponse() {
    super();
  }

  public BaseResponse(Status status, String explanation) {
    this.explanation = explanation;
    setStatus(status);
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Status status) {
    this.status = status;
    this.success = false;
    switch (this.status) {
    case OK:
    case CREATED:
    case PARTIAL_CONTENT:
    case NO_CONTENT:
      this.success = true;
      break;
    default:
      this.success = false;
    }
  }

  /**
   * @return the status
   */
  public Status getStatus() {
    return status;
  }

  /**
   * @return the contentLength
   */
  public Long getContentLength() {
    return contentLength;
  }

  /**
   * @param contentLength the contentLength to set
   */
  public void setContentLength(Long contentLength) {
    this.contentLength = contentLength;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * @return the hcpTime
   */
  public Long getTime() {
    return time;
  }

  /**
   * @param time the hcpTime to set
   */
  public void setTime(Long time) {
    this.time = time;
  }

  /**
   * Returns whether the creation command was successful or not
   * @return If we were successful
   */
  public Boolean isSuccess() {
    return success;
  }

  /**
   * @param success the success to set
   */
  public void setSuccess(Boolean success) {
    this.success = success;
  }

  /**
   * @return the inputStream
   */
  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * @param inputStream the inputStream to set
   */
  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  /**
   * For a failure, shows the explanation of the failure. null for success.
   * @return The String explanation message for the failure
   */
  public String getExplanation() {
    return explanation;
  }

  /**
   * @param explanation the explanation to set
   */
  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  /**
   * @return the requestId
   */
  public String getRequestId() {
    return requestId;
  }

  /**
   * @param requestId the requestId to set
   */
  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  /**
   * @return the servicedBy
   */
  public String getServicedBy() {
    return servicedBy;
  }

  /**
   * @param servicedBy the servicedBy to set
   */
  public void setServicedBy(String servicedBy) {
    this.servicedBy = servicedBy;
  }
}