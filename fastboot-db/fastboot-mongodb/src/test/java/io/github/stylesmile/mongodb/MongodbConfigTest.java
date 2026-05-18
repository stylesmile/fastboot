package io.github.stylesmile.mongodb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MongodbConfig.
 */
public class MongodbConfigTest {

    @Before
    public void setUp() {
        System.setProperty("mongodb.db", "testdb");
        System.setProperty("mongodb.host", "localhost");
        System.setProperty("mongodb.port", "27017");
        System.setProperty("mongodb.username", "admin");
        System.setProperty("mongodb.password", "password");
    }

    @After
    public void tearDown() {
        System.clearProperty("mongodb.db");
        System.clearProperty("mongodb.host");
        System.clearProperty("mongodb.port");
        System.clearProperty("mongodb.username");
        System.clearProperty("mongodb.password");
    }

    @Test
    public void testGetMongoDatabaseWithoutConnection() {
        try {
            MongodbConfig.getMongoDatabase();
            fail("Should throw exception when no MongoDB server is available");
        } catch (Exception e) {
            // Expected - no MongoDB server available in unit test scope
            assertTrue(e instanceof Exception);
        }
    }
}