package org.androidcru.crucentralcoast.mocking;

import org.androidcru.crucentralcoast.data.providers.ApiProvider;
import org.junit.Before;
import org.junit.Rule;

import okhttp3.mockwebserver.MockWebServer;

public class ServerTest
{
    @Rule
    public MockWebServer server = new MockWebServer();

    @Before
    public void setUp()
    {
        ApiProvider.setBaseUrl(server.url("/").toString());
    }
}
