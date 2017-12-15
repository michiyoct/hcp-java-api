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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Represents a high level connection to HCP. Abstracts out the underlying connection handling.
 * 
 * Internal class - should never be referenced directly in code. @see ConnectionFactory for how to create a Connection instance
 * or @see IConnection for available methods.
 * 
 * It is presumed that a single Thread (E.g. a Pentaho Data Integration Step instance) will
 * hold a copy of this connection object for it's lifecycle. This class will manage the 
 * underlying connection pooling and claiming/releasing of connections as required by its methods.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2017-12-08
 */
public class HttpConnection implements IConnection {
  private String namespace;
  private String tenant;
  private String host;
  //private String username;
  //private String password;

  private String authHeader;
  private String basePath;

  private final HttpContext context;
  
  private CloseableHttpClient httpClient = null;

  protected HttpConnection(String namespace,String tenant,String host,String username,String password) {
    this.namespace = namespace;
    this.tenant = tenant;
    this.host = host;
    //this.username = username;
    //this.password = password;

    context = HttpClientContext.create();

    Base64.Encoder encoder = Base64.getEncoder();
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes());

      authHeader = "hcp-ns-auth: " + encoder.encodeToString(username.getBytes()) + ":" + md.digest();

      basePath = "https://" + this.namespace + "/" + this.tenant + "/" + this.host;
    } catch (NoSuchAlgorithmException nsae) {
      System.err.println(nsae);
    }

    prepare();
  }

  public void close() throws HCPException {
    try {
      httpClient.close();
    } catch (IOException ioe) {
      throw new HCPException("Error closing connection",ioe);
    }
  }

  private void prepare() {
    if (null == httpClient) {
      httpClient = ConnectionFactory.getClient();
    }
  }

  /**
   * Creates an object on HCP Server with the given path and name, and String content
   */
  public CreateResponse createObject(String objectPath,String fileContent) throws HCPException {
    try {
      return createObject(objectPath,new StringEntity(fileContent));
    } catch (UnsupportedEncodingException uee) {
      throw new HCPException("Error creating request content from String",uee);
    }
  }

  /**
   * Creates an object on HCP Server with the given path and name, and File content
   */
  public CreateResponse createObject(String objectPath, File fileContent) throws HCPException {
    return createObject(objectPath, new FileEntity(fileContent));
  }

  /**
   * Creates an object on HCP Server with the given path and name, and InputStream content
   */
  public CreateResponse createObject(String objectPath, InputStream fileContent) throws HCPException {
    return createObject(objectPath, new InputStreamEntity(fileContent));
  }

  /**
   * Creates an object on HCP Server with the given path and name, and Java Serializable object content
   */
  public CreateResponse createObject(String objectPath, Serializable fileContent) throws HCPException {
    return createObject(objectPath, new SerializableEntity(fileContent));
  }

  /**
   * Creates the actual object, whatever the content source
   */
  private CreateResponse createObject(String objectPath,HttpEntity entity) throws HCPException {
    CreateResponse cr;
    
    //CloseableHttpClient httpClient = HttpClients.createDefault(); // comment this out to use the pooling client
    try {
      HttpPut httpput = new HttpPut(basePath + "/rest" + objectPath);

      //try {
      httpput.addHeader("Cookie", authHeader);
      httpput.setEntity(entity);

      System.out.println("executing request " + httpput.getRequestLine());
      CloseableHttpResponse response = httpClient.execute(httpput, context);

      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          System.out.println("Response content length: " + resEntity.getContentLength());
        }
        EntityUtils.consume(resEntity);

        // Check response code
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == 201) {
          // success
          // Check headers for response
          Header[] locationHeaders = response.getHeaders("Location");
          String location = "";
          if (null != locationHeaders && locationHeaders.length > 0 ) {
            location = locationHeaders[0].getValue();
          }
          Header[] hashHeaders = response.getHeaders("X-ArcHash");
          String hashHeader = "";
          if (null != hashHeaders && hashHeaders.length > 0) {
            hashHeader = hashHeaders[0].getValue();
          }
          // parse out header content and value
          String[] hashParts = hashHeader.split(" ");
          String hashValue = "";
          // get SHA-256 value
          hashValue = hashParts[1]; // first part is the hash algorithm used (SHA-256)
          Header[] clusterTimeHeaders = response.getHeaders("X-ArcClusterTime");
          long clusterTime = -1L;
          if (null != clusterTimeHeaders && clusterTimeHeaders.length > 0) {
            clusterTime = Long.valueOf(clusterTimeHeaders[0].getValue());
          }
          Header[] versionIdHeaders = response.getHeaders("X-HCP-VersionId");
          String versionId = null;
          if (null != versionIdHeaders && versionIdHeaders.length>0) {
            versionId = versionIdHeaders[0].getValue();
          }
          cr = new CreateResponse(CreateResponse.Status.CREATED,location,hashValue,clusterTime,versionId);
        } else if (status.getStatusCode() == 403) {
          // forbidden
          cr = new CreateResponse(CreateResponse.Status.FORBIDDEN,status.getReasonPhrase());
        } else if (status.getStatusCode() == 409) {
          // conflict
          cr = new CreateResponse(CreateResponse.Status.CONFLICT,status.getReasonPhrase());
        } else if (status.getStatusCode() == 413) {
          // File too large
          cr = new CreateResponse(CreateResponse.Status.TOO_LARGE,status.getReasonPhrase());
        } else {
          // unknown
          cr = new CreateResponse(CreateResponse.Status.OTHER_FAILURE,status.getReasonPhrase());
        }
      } catch (IOException ioe) {
        throw new HCPException("Exception accessing server response object", ioe);
      } finally {
        response.close();
      }
      //} finally {
      //  httpclient.close(); // comment this out when using connection pooled client
      //}
    } catch (IOException ioe) {
      throw new HCPException("Exception issuing request to HCP", ioe);
    }

    return cr;
  }
}