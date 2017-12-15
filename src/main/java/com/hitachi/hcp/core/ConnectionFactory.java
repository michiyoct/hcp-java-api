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

import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Allows the creation of an underlying connection to a HCP Server.
 * 
 * This class wraps high-level connection pooling functionality, and is thread safe.
 * 
 * @author Adam Fowler <adam.fowler@hitachivantara.com>
 * @since 2017-12-15
 */
public class ConnectionFactory {
  private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

  /**
   * Creates a new IConnection instance using the provided server and authentication information
   * 
   * @param namespace The HCP Namespace to use
   * @param tenant The HCP Tenant to use
   * @param host The hostname (not including namespace and tenant) to communicate with
   * @param username The username for authentication (not held in plain text in RAM)
   * @param password The password for authentication (not held in plain text in RAM)
   */
  public static IConnection create(String namespace,String tenant,String host,String username,String password) {
    HttpConnection c = new HttpConnection(namespace,tenant,host,username,password);
    return c;
  }

  /**
   * Creates a new IConnection instance using the provided server and authentication information. Includes a user specified max connections setting (defaults to 20 otherwise).
   * 
   * @param namespace The HCP Namespace to use
   * @param tenant The HCP Tenant to use
   * @param host The hostname (not including namespace and tenant) to communicate with
   * @param username The username for authentication (not held in plain text in RAM)
   * @param password The password for authentication (not held in plain text in RAM)
   * @param maxConnections The maximum number of parallel connections to have open to the server
   */
  public static IConnection create(String namespace,String tenant,String host,String username,String password,int maxConnections) {
    cm.setDefaultMaxPerRoute(maxConnections);
    HttpConnection c = new HttpConnection(namespace, tenant, host, username, password);
    return c;
  }

  /**
   * Protected method used by IConnection subclass instances in order to create an underlying HTTP connection. DO NOT USE.
   */
  protected static CloseableHttpClient getClient() {
    return HttpClients.custom().setConnectionManager(cm).build();
  }
}