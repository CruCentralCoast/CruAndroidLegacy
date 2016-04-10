package org.androidcru.crucentralcoast.data.providers;

import com.google.gson.reflect.TypeToken;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.common.ResourcesUtil;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestObserver;
import rx.schedulers.Schedulers;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.mockito.*", "javax.net.ssl.*", "android.*" })
@PrepareForTest(ApiProvider.class)
public class EventProviderTest
{
    @Rule
    MockWebServer server = new MockWebServer();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.immediate()))
            .build();

    CruApiService service = retrofit.create(CruApiService.class);

    @Before
    public void setUp()
    {
        PowerMockito.mockStatic(ApiProvider.class);
        Mockito.when(ApiProvider.getService()).thenReturn(service);
    }

    @Test
    public void testEvents()
    {
        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.enqueue(new MockResponse().setBody(eventsJson));
        ArrayList<CruEvent> events = CruApplication.gson.fromJson(eventsJson, new TypeToken<ArrayList<CruEvent>>(){}.getType());

        TestObserver<ArrayList<CruEvent>> observer = new TestObserver<>();
        ArrayList<ArrayList<CruEvent>> wrapper = new ArrayList<>();
        wrapper.add(events);
        EventProvider.requestEvents().toBlocking().subscribe(observer);
        observer.assertReceivedOnNext(wrapper);
    }
}