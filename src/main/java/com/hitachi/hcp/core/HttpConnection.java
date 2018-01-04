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

import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

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
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 * @since 1.0 2017-12-08
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

  protected HttpConnection(String namespace,String tenant,String host,String username,String password,boolean acceptSelfSigned) {
    this.namespace = namespace;
    this.tenant = tenant;
    this.host = host;
    //this.username = username;
    //this.password = password;

    context = HttpClientContext.create();

    Base64.Encoder encoder = Base64.getEncoder();
    //try {
      //MessageDigest md = MessageDigest.getInstance("MD5");
      //md.update(password.getBytes());

      String md5 = DigestUtils.md5Hex(password);
      authHeader = "hcp-ns-auth=" + encoder.encodeToString(username.getBytes()) + ":" + md5;
      System.out.println("Auth Header: " + authHeader);

      basePath = "https://" + this.namespace + "." + this.tenant + "." + this.host;
    //} catch (NoSuchAlgorithmException nsae) {
    //  System.err.println(nsae);
    //}

    prepare(acceptSelfSigned);
  }
  
  private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
      throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

    // use the TrustSelfSignedStrategy to allow Self Signed Certificates
    SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

    // we can optionally disable hostname verification. 
    // if you don't want to further weaken the security, you don't have to include this.
    HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

    // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
    // and allow all hosts verifier.
    SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

    // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
    return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
  }

  public void close() throws HCPException {
    try {
      httpClient.close();
    } catch (IOException ioe) {
      throw new HCPException("Error closing connection",ioe);
    }
  }

  private void prepare(boolean acceptSelfSigned) {
    if (null == httpClient) {
      try {
        if (acceptSelfSigned) {        
          httpClient = createAcceptSelfSignedCertificateClient();
        } else {
          httpClient = ConnectionFactory.getClient();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
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
    CreateResponse cr = new CreateResponse();
    
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

        // Check response code
        int status = readStatii(cr,response);
        if (201 == status) {
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
          hashValue = (null != hashParts && hashParts.length > 1) ? hashParts[1] : null; // first part is the hash algorithm used (SHA-256)
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
        } 
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          System.out.println("Response content length: " + resEntity.getContentLength());
        }
        EntityUtils.consume(resEntity);
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

  public ReadResponse readObject(String objectPath) throws HCPException {
    ReadResponse rr = new ReadResponse();
    
    try {
      HttpGet httpget = new HttpGet(basePath + "/rest" + objectPath);

      //try {
      httpget.addHeader("Cookie", authHeader);

      System.out.println("executing request " + httpget.getRequestLine());
      CloseableHttpResponse response = httpClient.execute(httpget, context);

      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());

        // Check response code
        int status = readStatii(rr,response);
        
        if (200 == status) {
          // read metadata
          populateSystemMetadata(rr,response);

          // read content
          HttpEntity resEntity = response.getEntity();
          if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
          }
          rr.setInputStream((new BufferedHttpEntity(resEntity)).getContent());
          //EntityUtils.consume(resEntity);
          // TODO handle when to release this connection due to client Java finishing reading the response... 
          //   Intermediate  InputStream wrapper? Something to handle this in BaseResponse? (close method?)
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

    return rr;
  }
  
  public ReadMetadataResponse readObjectMetadata(String objectPath) throws HCPException {
    ReadMetadataResponse rr = new ReadMetadataResponse();

    try {
      HttpHead httphead = new HttpHead(basePath + "/rest" + objectPath);

      //try {
      httphead.addHeader("Cookie", authHeader);

      System.out.println("executing request " + httphead.getRequestLine());
      CloseableHttpResponse response = httpClient.execute(httphead, context);

      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());

        // Check response code
        int status = readStatii(rr, response);

        if (200 == status) {
          // read metadata
          populateMinimumSystemMetadata(rr,response);
          populateSystemMetadata(rr, response);

          // read content
          HttpEntity resEntity = response.getEntity();
          if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
          }
          //rr.setInputStream((new BufferedHttpEntity(resEntity)).getContent());
          //EntityUtils.consume(resEntity);
          // TODO handle when to release this connection due to client Java finishing reading the response... 
          //   Intermediate  InputStream wrapper? Something to handle this in BaseResponse? (close method?)
        }

      } catch (Exception ioe) {
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

    return rr;
  }

  private int readStatii(BaseResponse br,CloseableHttpResponse response) {
    StatusLine status = response.getStatusLine();
    int statusCode = status.getStatusCode();
    if (statusCode == 200) {
      // success
      br.setStatus(BaseResponse.Status.OK);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 201) {
      // success
      br.setStatus(BaseResponse.Status.CREATED);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 204) {
      // success
      br.setStatus(BaseResponse.Status.NO_CONTENT);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 206) {
      // success
      br.setStatus(BaseResponse.Status.PARTIAL_CONTENT);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 302) {
      // success
      br.setStatus(BaseResponse.Status.AUTH_MISSING);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 400) {
      // success
      br.setStatus(BaseResponse.Status.BAD_REQUEST);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 401) {
      // success
      br.setStatus(BaseResponse.Status.UNAUTHORIZED);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 403) {
      // forbidden
      br.setStatus(BaseResponse.Status.FORBIDDEN);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 404) {
      // forbidden
      br.setStatus(BaseResponse.Status.NOT_FOUND);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 409) {
      // conflict
      br.setStatus(BaseResponse.Status.CONFLICT);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 413) {
      // File too large
      br.setStatus(BaseResponse.Status.FILE_TOO_LARGE);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 414) {
      // File too large
      br.setStatus(BaseResponse.Status.REQUEST_TOO_LARGE);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 416) {
      // File too large
      br.setStatus(BaseResponse.Status.REQUEST_RANGE_INVALID);
      br.setExplanation(status.getReasonPhrase());
    } else if (statusCode == 500) {
      // File too large
      br.setStatus(BaseResponse.Status.INTERNAL_ERROR);
      br.setExplanation(status.getReasonPhrase());
    } else {
      // unknown
      br.setStatus(BaseResponse.Status.OTHER_FAILURE);
      br.setExplanation(status.getReasonPhrase());
    }
    return statusCode;
  }

  private void populateMinimumSystemMetadata( BaseResponse br,CloseableHttpResponse response) {
    Header header = response.getFirstHeader("X-HCP-Time");
    if (null != header) {
      try {
        br.setTime(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
    header = response.getFirstHeader("X-HCP-ServicedBySystem");
    if (null != header) {
      br.setServicedBy(header.getValue());
    }
    header = response.getFirstHeader("X-RequestId");
    if (null != header) {
      br.setRequestId(header.getValue());
    }
    header = response.getFirstHeader("Content-Type");
    if (null != header) {
      br.setContentType(header.getValue());
    }
    header = response.getFirstHeader("Content-Length");
    if (null != header) {
      try {
        br.setContentLength(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
  }

  private void populateSystemMetadata( ReadMetadataResponse rmr, CloseableHttpResponse response ) {
    Header header = response.getFirstHeader("X-HCP-Type");
    if (null != header) {
      rmr.setType(header.getValue());
    }
    header = response.getFirstHeader("X-HCP-Size");
    if (null != header) {
      try {
        rmr.setSize(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
    header = response.getFirstHeader("X-HCP-Hash");
    if (null != header) {
      String[] parts = header.getValue().split(" ");
      if (parts.length < 2) { // check to see if the hash header is DFEB3465... or SHA-256 DFEB3465...
        rmr.setHash(header.getValue());
      } else {
        rmr.setHash(parts[1]);
        rmr.setHashScheme(parts[0]);
      }
    }
    header = response.getFirstHeader("X-HCP-VersionId");
    if (null != header) {
      rmr.setVersionId(header.getValue());
    }
    header = response.getFirstHeader("X-HCP-IngestTime");
    if (null != header) {
      try {
        rmr.setIngestTime(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
    header = response.getFirstHeader("X-HCP-RetentionClass");
    if (null != header) {
      rmr.setRetentionClass(header.getValue());
    }
    header = response.getFirstHeader("X-HCP-RetentionString");
    if (null != header) {
      rmr.setRetentionString(header.getValue());
    }
    header = response.getFirstHeader("X-HCP-Retention");
    if (null != header && !"".equals(header.getValue())) {
      try {
        rmr.setRetention(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
    header = response.getFirstHeader("X-HCP-RetentionHold");
    if (null != header && !"".equals(header.getValue())) {
      if ("false".equals(header.getValue())) {
        rmr.setRetentionHold(Boolean.FALSE);
      } else if ("true".equals(header.getValue())) {
        rmr.setRetentionHold(Boolean.TRUE);
      }
    }
    header = response.getFirstHeader("X-HCP-Shred");
    if (null != header && !"".equals(header.getValue())) {
      if ("false".equals(header.getValue())) {
        rmr.setShred(Boolean.FALSE);
      } else if ("true".equals(header.getValue())) {
        rmr.setShred(Boolean.TRUE);
      }
    }
    header = response.getFirstHeader("X-HCP-DPL");
    if (null != header && !"".equals(header.getValue())) {
      try {
        rmr.setDpl(Long.parseLong(header.getValue()));
      } catch (NumberFormatException nfe) {
        // TODO log this in the future, but continue anyway
      }
    }
    header = response.getFirstHeader("X-HCP-Index");
    if (null != header && !"".equals(header.getValue())) {
      if ("false".equals(header.getValue())) {
        rmr.setIndexed(Boolean.FALSE);
      } else if ("true".equals(header.getValue())) {
        rmr.setIndexed(Boolean.TRUE);
      }
    }
    header = response.getFirstHeader("X-HCP-Custom-Metadata");
    if (null != header && !"".equals(header.getValue())) {
      if ("false".equals(header.getValue())) {
        rmr.setCustomMetadata(Boolean.FALSE);
      } else if ("true".equals(header.getValue())) {
        rmr.setCustomMetadata(Boolean.TRUE);
      }
    }
  }

  private void populateCustomMetadata( ReadMetadataResponse rmr, CloseableHttpResponse response ) {
    // TODO complete this method for all custom metadata
  }
  
  public DeleteResponse deleteObject(String objectPath) throws HCPException {
    DeleteResponse dr = new DeleteResponse();

    try {
      HttpDelete httpDelete = new HttpDelete(basePath + "/rest" + objectPath);

      //try {
      httpDelete.addHeader("Cookie", authHeader);

      System.out.println("executing request " + httpDelete.getRequestLine());
      CloseableHttpResponse response = httpClient.execute(httpDelete, context);

      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());

        // Check response code
        int status = readStatii(dr, response);

        if (200 == status) {
          // read response headers
          // print headers for test (Documentation doesn't include Delete response examples...)
          //printHeaders(response);
          populateMinimumSystemMetadata(dr, response);
        }

      } catch (Exception ioe) {
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

    return dr;
  }

  public ListResponse listObjects(String folderPath) throws HCPException {
    ListResponse lr = new ListResponse();

    String newFolderPath = new String(folderPath);
    if (!newFolderPath.endsWith("/")) {
      newFolderPath += "/";
    }

    try {
      HttpGet httpGet = new HttpGet(basePath + "/rest" + newFolderPath);

      //try {
      httpGet.addHeader("Cookie", authHeader);

      System.out.println("executing request " + httpGet.getRequestLine());
      CloseableHttpResponse response = httpClient.execute(httpGet, context);

      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());

        // Check response code
        int status = readStatii(lr, response);

        if (200 == status) {
          // read response headers
          // print headers for test (Documentation doesn't include Delete response examples...)
          //printHeaders(response);
          populateMinimumSystemMetadata(lr, response);

          // parse XML for entries...
          HttpEntity entity = response.getEntity();
          /*
          InputStream as = entity.getContent();
          StringBuffer sb = new StringBuffer();
          byte[] buffer = new byte[8192];
          int len;
          while (-1 != (len = as.read(buffer))) {
            for (int i = 0;i < len;i++) {
              sb.append((char)buffer[i]);
            }
          }
          as.close();
          System.out.println(sb.toString());
          //readEntriesXml(lr,sb.toString());
          */
          
          readEntriesXml(lr,entity.getContent());
        }

      } catch (Exception ioe) {
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

    return lr;
  }

  private void printHeaders(CloseableHttpResponse response) {
    for (Header header: response.getAllHeaders()) {
      System.out.println(header.getName() + " = " + header.getValue());
    }
  }

  private void readEntriesXml(ListResponse resp,InputStream is) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    
  	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(is);
    
    doc.getDocumentElement().normalize();

    Element dirElement = doc.getDocumentElement();
    resp.setUtf8Path(dirElement.getAttribute("utf8Path"));
    resp.setPath(dirElement.getAttribute("path"));
    resp.setParentDir(dirElement.getAttribute("parentDir"));
    String value = dirElement.getAttribute("dirDeleted");
    if (null != value && !"".equals(value)) {
      if ("false".equals(value)) {
        resp.setDirDeleted(Boolean.FALSE);
      } else if ("true".equals(value)) {
        resp.setDirDeleted(Boolean.TRUE);
      }
    }
    value = dirElement.getAttribute("showDeleted");
    if (null != value && !"".equals(value)) {
      if ("false".equals(value)) {
        resp.setShowDeleted(Boolean.FALSE);
      } else if ("true".equals(value)) {
        resp.setShowDeleted(Boolean.TRUE);
      }
    }
    //System.out.println("Directory path: " + dirElement.getAttribute("path"));
    
    NodeList nList = doc.getElementsByTagName("entry");
    
    //System.out.println("----------------------------");
    Entry entry;

    for (int temp = 0; temp < nList.getLength(); temp++) {

      Node nNode = nList.item(temp);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {

        Element eElement = (Element) nNode;

        //System.out.println("urlName: " + eElement.getAttribute("urlName"));
        entry = new Entry();
        
        try {
          entry.setChangeTime(Long.parseLong(eElement.getAttribute("changeTimeMilliseconds")));
        } catch (NumberFormatException nfe) {
          // ignore this
        }
        entry.setChangeTimeString(eElement.getAttribute("changeTimeString"));
        value = eElement.getAttribute("customMetadata");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setCustomMetadata(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setCustomMetadata(Boolean.TRUE);
          }
        }
        value = eElement.getAttribute("customMetadataAnnotations");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setCustomMetadataAnnotations(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setCustomMetadataAnnotations(Boolean.TRUE);
          }
        }
        entry.setDomain(eElement.getAttribute("domain"));
        entry.setDpl(eElement.getAttribute("dpl"));
        entry.setEtag(eElement.getAttribute("etag"));
        value = eElement.getAttribute("hasAcl");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setHasAcl(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setHasAcl(Boolean.TRUE);
          }
        }
        entry.setHash(eElement.getAttribute("hash"));
        entry.setHashScheme(eElement.getAttribute("hashScheme"));
        value = eElement.getAttribute("hold");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setHold(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setHold(Boolean.TRUE);
          }
        }
        value = eElement.getAttribute("index");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setIndexed(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setIndexed(Boolean.TRUE);
          }
        }
        try {
          entry.setIngestTime(Long.parseLong(eElement.getAttribute("ingestTime")));
        } catch (NumberFormatException nfe) {
          // ignore this
        }
        entry.setIngestTimeString(eElement.getAttribute("ingestTimeString"));
        entry.setOwner(eElement.getAttribute("owner"));
        value = eElement.getAttribute("replicated");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setReplicated(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setReplicated(Boolean.TRUE);
          }
        }
        try {
          entry.setRetention(Long.parseLong(eElement.getAttribute("retention")));
        } catch (NumberFormatException nfe) {
          // ignore this
        }
        entry.setRetentionClass(eElement.getAttribute("retentionClass"));
        entry.setRetentionString(eElement.getAttribute("retentionString"));
        value = eElement.getAttribute("shred");
        if (null != value && !"".equals(value)) {
          if ("false".equals(value)) {
            entry.setShred(Boolean.FALSE);
          } else if ("true".equals(value)) {
            entry.setShred(Boolean.TRUE);
          }
        }
        try {
          entry.setSize(Long.parseLong(eElement.getAttribute("size")));
        } catch (NumberFormatException nfe) {
          // ignore this
        }
        entry.setState(eElement.getAttribute("state"));
        entry.setType(eElement.getAttribute("type"));
        
        entry.setUrlName(eElement.getAttribute("urlName"));
        
        entry.setUtf8Name(eElement.getAttribute("utf8Name"));
        entry.setVersion(eElement.getAttribute("version"));
        try {
          entry.setVersionCreateTime(Long.parseLong(eElement.getAttribute("versionCreateTimeMilliseconds")));
        } catch (NumberFormatException nfe) {
          // ignore this
        }
        

        resp.add(entry);

      }
    }

    System.out.println("----------------------------");

    is.close();
  }

}