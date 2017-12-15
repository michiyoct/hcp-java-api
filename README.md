# HCP Java API

Java API wrapper around the Hitachi Content Platform (HCP) REST API

This API was created in order to abstract the underlying details of how connection pooling and HTTP request handling worked for HCP away from a Pentaho Data Integration (PDI) extension for working with HCP. This API aims to be a best practice implementation of a Java wrapper for HCP's REST API, including support for advanced features. It has been created with highly parallel workloads in mind, and is tested for this purpose.

This API can be used independently of Pentaho for your own Java applications, and is licensed under the Apache 2.0 license.

## Design

A ConnectionFactory object provides static methods to create instances of the IConnection interface. This sets up high level connection handling options, including protocol (default is http), number of connections, and connection pool management.

An instance of IConnection is returned from ConnectionFactory.create(). This instance provides methods for interacting with HCP that completely abstract out HTTP request and response handling. This makes the API very easy to use for those who are not experts in REST API design and performance.

Each method in the IConnection instance returns an object encapsulating the possible response fields for that request. IConnection.createObject(), for example, returns a CreateResponse object indicating the status and success of the response, and any metadata fields returned from HCP. For this method, this includes the file Location, SHA-256 hash, cluster timestamp, and versionId (if versioning is enabled).

After each IConnection function call the underlying connection is immediately returned to the pool.

## Quick Start

The below code sample shows creating a new connection pool, using a connection, and creating an object on the HCP Server:-

```java
try {
  IConnection conn = ConnectionFactory.create("default","default","hcp.localdomain.com","myuser","mypass");
  CreateResponse resp = conn.createObject("/some/object.txt","Lorem ipsum dolar sit amet");
  if (resp.isSuccessful()) {
    System.out.println("Success! : " + resp.getLocation());
  } else {
    System.out.println("Failure! : " + resp.getExplanation());
  }
} catch (HCPException hcpe) {
  hcpe.printStackTrace();
}
```

You can also use multiple threads with the ConnectionFactory, assigning each Thread it's own IConnection:-

```java
try {
    int perThread = 100;
    int threads = 10;
    for (int i = 0;i < threads;i++) {
      IConnection conn = ConnectionManager.getConnection(); // one per thread for our testing purposes
      CreateObjectThread thread = new CreateObjectThread(conn,i * perThread,((i + 1)*perThread)-1,"Wibble");
      thread.run();
    }
} catch (HCPException hcpe) {
  hcpe.printStackTrace();
}
```

This would use a CreateObjectThread wrapper like the below. A full example is available in test/java/com/hitachi/hcp/test/util/CreateObjectThread.java.

```java
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
```

The manager itself manages the connection pool, ensuring that all Threads share the same pool of connections. The IConnection instance implementation itself asks for a connection, uses it, and relinquishes the connection with each function call.

## Errata

Feature Requests, bugs and questions can be sent to https://github.com/Pentaho-SE-EMEA-APAC/hcp-java-api/issues

## License and Copyright statement

Unless otherwise stated, all material in this repository are Copyright Pentaho 2002-2017, and licensed under the Apache-2.0 license.
