package org.androidcru.crucentralcoast.tests;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.mocking.ServerTest;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.junit.Rule;
import org.junit.Test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.androidcru.crucentralcoast.tests.DriverSignupTests.clickChildViewWithId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by main on 4/27/2016.
 */
public class AlertDialogTests extends ServerTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private void switchToScreen(int viewID)
    {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(viewID)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    private Dispatcher getNewDispatcher() {
        Dispatcher dispatch = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/api/events/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json"));
                else if (request.getPath().equals("/api/rides/") && request.getMethod().equals("GET"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
                else if (request.getPath().equals("/api/rides/571d83c497219fa4327ee8ea/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
                else if (request.getPath().equals("/api/events/find"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json"));
                else if (request.getPath().equals("/api/rides/find") && request.getMethod().equals("POST"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
                else if (request.getPath().equals("/api/rides/571d83c497219fa4327ee8ea"))
                    return new MockResponse().setResponseCode(200);
                else if (request.getPath().equals("/api/passengers/find"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "onePassenger.json"));
                else if(request.getPath().equals("/api/rides/571d83c497219fa4327ee8ea/passengers/5722974f97219fa4327ee8f1"))
                    return new MockResponse().setResponseCode(200);
                else {
                    System.out.println("ERROR BAD REQUEST AHHH: " + request.getPath() + " " + request.getMethod());
                    return new MockResponse().setResponseCode(404);
                }
            }
        };
        return dispatch;
    }

    @Test
    public void testMyRideCancel() throws UiObjectNotFoundException {
        server.setDispatcher(getNewDispatcher());
        switchToScreen(R.string.nav_my_rides);

        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.cancelOffering)));

        //verify Alert dialog text is displayed
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(new UiSelector().description("Cancel Ride"));
        device.findObject(new UiSelector().description("Are you sure you want to cancel this ride?"));

        //verify NO button doesn't cancel
        onView(withText("No")).perform(click());

        //bring up dialog again
        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.cancelOffering)));

        //verify YES button sends the correct response cancel
        onView(withText("Yes")).perform(click());
    }

    @Test
    public void testMyRideInfoKick() throws UiObjectNotFoundException {
        server.setDispatcher(getNewDispatcher());
        switchToScreen(R.string.nav_my_rides);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(new UiSelector().textContains("Fall Retreat")).click();

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.kickPassenger)));

        device.findObject(new UiSelector().description("Kick Passenger"));
        device.findObject(new UiSelector().description("Are you sure you want to kick"));

        //click no
        onView(withText("No")).perform(click());

        //bring up dialog again
        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.kickPassenger)));

        //verify YES button sends the correct response cancel
        onView(withText("Yes")).perform(click());
    }

    @Test
    public void testRidesharingDialog() throws UiObjectNotFoundException {
        server.setDispatcher(getNewDispatcher());
        switchToScreen(R.string.nav_events);

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.rideSharingButton)));

        //verify Alert dialog text is displayed
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(new UiSelector().description("Ride Sharing"));
        device.findObject(new UiSelector().description("For Fall Retreat, would you like to be a Driver or a Passenger?"));

        //click on the Driver part of the alert dialog
        onView(withText("DRIVER")).perform(click());

        pressBack();

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.rideSharingButton)));

        //click on the Driver part of the alert dialog
        onView(withText("PASSENGER")).perform(click());
    }
}
