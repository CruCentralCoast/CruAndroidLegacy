package org.androidcru.crucentralcoast.mocking;


import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

public class ServerTest
{
    public static MockWebServer server = new MockWebServer();

    @BeforeClass
    public static void setUp() throws IOException
    {
        server.start();
        CruApiProvider.setBaseUrl(server.url("/").toString());
    }

    @AfterClass
    public static void tearDown() throws IOException
    {
        server.shutdown();
    }
}
