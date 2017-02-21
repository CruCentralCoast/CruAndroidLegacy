package org.androidcru.crucentralcoast.data.providers;

import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.data.mocking.ServerTest;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.threeten.bp.Month;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import rx.observers.TestObserver;
import rx.schedulers.Schedulers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventProviderTest extends ServerTest
{
    Dispatcher dispatcher = new Dispatcher()
    {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
            String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
            return new MockResponse().setBody(eventsJson);
        }
    };

    @Test
    public void testEvents() throws IOException
    {
        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.setDispatcher(dispatcher);

        ArrayList<CruEvent> events = CruApplication.gson.fromJson(eventsJson, new TypeToken<ArrayList<CruEvent>>(){}.getType());

        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        ArrayList<List<CruEvent>> wrapper = new ArrayList<>();
        wrapper.add(events);
        EventProvider.requestAllEvents()
                .toBlocking()
                .subscribe(observer);
        observer.assertTerminalEvent();
        observer.assertReceivedOnNext(wrapper);
    }

    @Test
    public void testEventsWithSharedPreferencesEmpty() throws IOException
    {
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getBoolean("563b08df2930ae0300fbc0a0", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b08fb2930ae0300fbc0a1", false)).thenReturn(false);
        when(sharedPreferences.getBoolean("563b090b2930ae0300fbc0a2", false)).thenReturn(false);
        server.setDispatcher(dispatcher);

        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        EventProvider.requestUsersEvents()
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

        server.setDispatcher(dispatcher);

        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        EventProvider.requestUsersEvents()
                .subscribeOn(Schedulers.immediate())
                .toBlocking()
                .subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().size(), is(1));
    }

    @Test
    public void testPaginatedEventsErrors() throws Exception
    {
        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        EventProvider.getEventsPaginated(null, -1, -1).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().isEmpty(), is(true));
        assertThat(observer.getOnErrorEvents(), hasItem(instanceOf(IllegalArgumentException.class)));

        observer = new TestObserver<>();
        EventProvider.getEventsPaginated(ZonedDateTime.now(), -1, -1).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().isEmpty(), is(true));
        assertThat(observer.getOnErrorEvents(), hasItem(instanceOf(IllegalArgumentException.class)));

        observer = new TestObserver<>();
        EventProvider.getEventsPaginated(ZonedDateTime.now(), 0, -1).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().isEmpty(), is(true));
        assertThat(observer.getOnErrorEvents(), hasItem(instanceOf(IllegalArgumentException.class)));

        observer = new TestObserver<>();
        EventProvider.getEventsPaginated(ZonedDateTime.now(), 0, 0).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        assertThat(observer.getOnNextEvents().isEmpty(), is(true));
        assertThat(observer.getOnErrorEvents(), hasItem(instanceOf(IllegalArgumentException.class)));
    }

    @Test
    public void testPaginatedEventsBasic() throws Exception
    {
        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        server.setDispatcher(dispatcher);

        ZonedDateTime zdt = ZonedDateTime.of(2016, Month.MAY.getValue(), 4, 8, 0, 0, 0, ZoneOffset.UTC);
        EventProvider.getEventsPaginated(zdt, 0, 1).subscribeOn(Schedulers.immediate()).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        String expected = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "queries/events1.json");
        String actual = ResourcesUtil.convertStreamToString(server.takeRequest().getBody().inputStream());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testPaginatedEventsWithPages() throws Exception
    {
        TestObserver<List<CruEvent>> observer = new TestObserver<>();
        server.setDispatcher(dispatcher);

        ZonedDateTime zdt = ZonedDateTime.of(2016, Month.MAY.getValue(), 4, 8, 0, 0, 0, ZoneOffset.UTC);
        EventProvider.getEventsPaginated(zdt, 2, 20).toBlocking().subscribe(observer);
        observer.assertTerminalEvent();
        String expected = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "queries/events2.json");
        String actual = ResourcesUtil.convertStreamToString(server.takeRequest().getBody().inputStream());
        JSONAssert.assertEquals(expected, actual, false);
    }
}