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
package com.hitachi.hcp.test.util;

import com.hitachi.hcp.core.IConnection;
import com.hitachi.hcp.core.HCPException;

public class CreateObjectThread extends Thread {

  private final IConnection connection;
  private int min;
  private int max;
  private String content;

  private boolean successful = false;

  public CreateObjectThread(IConnection connection,int min,int max, String content) {
    this.connection = connection;
    this.min = min;
    this.max = max;
    this.content = content;
  }

  public void run() {
    int i = 0;
    try {
      boolean success = true;
      for (i = min;i <= max;i++) {
        success = success & connection.createObject("/object" + i + ".txt",content).isSuccessful();
      }
      successful = success;
    } catch (HCPException hcpe) {
      System.err.println("Error creating object " + i);
      hcpe.printStackTrace();
    }
  }

  public boolean isSuccessful() {
    return successful;
  }
}