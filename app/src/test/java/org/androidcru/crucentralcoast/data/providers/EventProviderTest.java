package org.androidcru.crucentralcoast.data.providers;

import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.mocking.ServerTest;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import rx.observers.TestObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventProviderTest extends ServerTest
{
    @Test
    public void testEvents() throws IOException
    {
        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.enqueue(new MockResponse().setBody(eventsJson));

        ArrayList<CruEvent> events = CruApplication.gson.fromJson(eventsJson, new TypeToken<ArrayList<CruEvent>>(){}.getType());

        TestObserver<ArrayList<CruEvent>> observer = new TestObserver<>();
        ArrayList<ArrayList<CruEvent>> wrapper = new ArrayList<>();
        wrapper.add(events);
        EventProvider.requestAllEvents()
                .toBlocking()
                .subscribe(observer);
        observer.assertTerminalEvent();
        observer.assertReceivedOnNext(wrapper);
    }

    @Test
    //@Ignore
    public void testEventsWithSharedPreferencesEmpty() throws IOException
    {
        CruApiProvider.setBaseUrl(server.url("/").toString());

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getBoolean("563b08df2930ae0300fbc0a0", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b08fb2930ae0300fbc0a1", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b090b2930ae0300fbc0a2", false)).thenReturn(false);

        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.enqueue(new MockResponse().setBody(eventsJson));

        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        EventProvider.requestUsersEvents(sharedPreferences)
                .toBlocking()
                .subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().size(), is(0));

    }

    @Test
    public void testEventsWithSharedPreferences() throws IOException
    {

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getBoolean("563b08df2930ae0300fbc0a0", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b08fb2930ae0300fbc0a1", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b090b2930ae0300fbc0a2", false)).thenReturn(true);

        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.enqueue(new MockResponse().setBody(eventsJson));

        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        EventProvider.requestUsersEvents(sharedPreferences)
                .toBlocking()
                .subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().size(), is(1));

    }
}