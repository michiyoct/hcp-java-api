/**
 * Provides a high-level API of use to application developers who need to interact with the Hitachi Content Platform (HCP).
 * <p>
 * This API provides connection pooling and management, and abstracts the underlying connection protocols from the client.
 * This means you have to use the {@link com.hitachi.hcp.core.ConnectionFactory} class in order to create connections.
 * <p>
 * You can keep a single Connection instance available per thread. ALL threads will still share their underlying connections,
 * and retrieve them from and return them to the pool.
 * <p>
 * A Connection instance will implement the {@link com.hitachi.hcp.core.IConnection} interface. The methods in this class are
 * communication protocol independent. 
 * <p>
 * Each call to a method within an IConnection instance will return one of the subclasses of the {@link com.hitachi.hcp.core.BaseResponse} class. This Response object will contain generic response information from the server (such as server time, and request Id), and call-specific information - hence the subclasses.
 * <p>
 * For example, {@link com.hitachi.hcp.core.ReadResponse} provides a method called {@link com.hitachi.hcp.core.ReadResponse#getInputStream()} which returns an input stream pointing to the content of the response. I.e. the content of the object you've just read from HCP.
 * <p>
 * It should be noted that access to the response object is independent of the underlying connection mechanism. Thus Response objects, and the results of method calls to their getters, can safely be stored in long term memory without worrying about the closing of the underlying connection removing access to the data.
 * <p>
 * This HCP Java API provides a HTTPS connection mechanism by default. Unsecured HTTP is not supported. The H3 protocol is not currently supported either.
 * <p>
 * Note that if you are using this API in a test environment it is highly likely your servers will use self-signed certificates. In such a case you will need to disable site validation. To do this, pass true in to the last parameter to your {@link com.hitachi.hcp.core.ConnectionFactory#create(String, String, String, String, String, int, boolean)} call.
 * <p>
 * WARNING: DISABLING SITE VALIDATION ON A PRODUCTION APPLICATION WILL MEAN YOUR APPLICATION IS VULNERABLE TO MAN-IN-THE-MIDDLE ATTACKS. ONLY DO THIS IN TESTING, AND NEVER IN PRODUCTION. USE FALSE AS THE LAST PARAMETER TO CREATE IN PRODUCTION.
 * 
 * @since 1.0 2018-01-04
 * @author Adam Fowler {@literal <adam.fowler@hitachivantara.com>}
 */
package com.hitachi.hcp.core;