package com.hitachi.hcp.core;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

public class Connection {
  private String namespace;
  private String tenant;
  private String host;
  private String username;
  private String password;

  private String authHeader;
  private String basePath;

  public Connection(String namespace,String tenant,String host,String username,String password) {
    this.namespace = namespace;
    this.tenant = tenant;
    this.host = host;
    this.username = username;
    this.password = password;

    Base64.Encoder encoder = Base64.getEncoder();
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(this.password.getBytes());

      authHeader = encoder.encodeToString(this.username.getBytes()) + ":" + md.digest();

      basePath = "https://" + this.namespace + "/" + this.tenant + "/" + this.host;
    } catch (NoSuchAlgorithmException nsae) {
      System.err.println(nsae);
    }
  }

  /**
   * Creates an object on HCP Server
   */
  public boolean createObject(String objectPath,String fileContent) throws HCPException {

    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpPut httpput = new HttpPut(basePath + "/rest" + objectPath);

      try {
        httpput.addHeader("hcp-ns-auth", authHeader);
        httpput.setEntity(new StringEntity(fileContent));

        System.out.println("executing request " + httpput.getRequestLine());
        CloseableHttpResponse response = httpclient.execute(httpput);
        try {

          System.out.println("----------------------------------------");
          System.out.println(response.getStatusLine());
          HttpEntity resEntity = response.getEntity();
          if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
          }
          EntityUtils.consume(resEntity);
        } finally {
          response.close();
        }
      } finally {
        httpclient.close();
      }
    } catch (IOException ioe) {
      throw new HCPException(ioe.getMessage());
    }

    return true;
  }
}