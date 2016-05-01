package org.androidcru.crucentralcoast.mocking;

import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
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
        CruApiProvider.setBaseUrl(server.url("/").toString());
    }
}
