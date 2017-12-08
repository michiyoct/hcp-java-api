package com.hitachi.hcp.test;

import com.hitachi.hcp.core.Connection;
import com.hitachi.hcp.core.HCPException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CreateObjectTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CreateObjectTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CreateObjectTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testCreateObject() throws HCPException
    {
        Connection conn = new Connection("default","default","localhost","user","pass");
        boolean success = conn.createObject("/CreateObjectTest.txt", "Baseic text file content here");
        
        assertTrue( true );
    }
}
