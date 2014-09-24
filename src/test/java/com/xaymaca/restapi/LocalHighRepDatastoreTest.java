package com.xaymaca.restapi;

import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;

public class LocalHighRepDatastoreTest {

    // maximum eventual consistency
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testEventuallyConsistentGlobalQueryResult() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key ancestor = KeyFactory.createKey("foo", 3);
        ds.put(new Entity("yam", ancestor));
        ds.put(new Entity("yam", ancestor));
        // global query doesn't see the data
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        // ancestor query does see the data
        assertEquals(2, ds.prepare(new Query("yam", ancestor)).countEntities(withLimit(10)));
    }
}