package com.crucentralcoast.app.data.mocking;


import com.crucentralcoast.app.data.providers.api.CruApiProvider;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockWebServer;
import rx.schedulers.Schedulers;

public class ServerTest
{
    protected static MockWebServer server = new MockWebServer();

    @BeforeClass
    public static void setUp() throws IOException
    {
        server.start();
        CruApiProvider.setBaseUrl(server.url("/").toString());
        CruApiProvider.setNetworkScheduler(Schedulers.io());
    }

    @Before
    public void clearServer() throws InterruptedException
    {
        if(server.getRequestCount() > 0)
            server.takeRequest(2, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() throws IOException
    {
        server.shutdown();
    }
}
