package org.androidcru.crucentralcoast.tests;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.common.NCVScrollToAction;
import org.androidcru.crucentralcoast.data.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.data.mocking.ServerTest;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by main on 3/10/2016.
 */
public class DriverSignupTests extends ServerTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private void switchToEvents()
    {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_events)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    private void switchToMyRides()
    {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_my_rides)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                if (v != null) {
                    v.performClick();
                }
            }
        };
    }

    private static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));

    }
    private static Matcher<Object> withToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    private Dispatcher getNewDispatcher() {
        Dispatcher dispatch = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/api/events/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json"));
                else if (request.getPath().equals("/api/rides/") && request.getMethod().equals("POST"))
                    return new MockResponse().setResponseCode(200);
                else if (request.getPath().equals("/api/rides/") && request.getMethod().equals("GET"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
                else if (request.getPath().equals("/api/rides/571d83c497219fa4327ee8ea/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
                else if (request.getPath().equals("/api/events/find"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json"));
                else if (request.getPath().equals("/api/rides/find") && request.getMethod().equals("POST"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "terbsRide.json"));
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
    public void testAddingRide() throws UiObjectNotFoundException {
        server.setDispatcher(getNewDispatcher());
        switchToEvents();

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.rideSharingButton)));

        //click on the Driver part of the alert dialog
        onView(withText("DRIVER")).perform(click());

        //enter a name
        onView(withId(R.id.name_field))
                .perform(replaceText("Terbius Blush"), closeSoftKeyboard());
        onView(withId(R.id.name_field))
                .check(matches(withText("Terbius Blush")));
        //enter a phone
        onView(withId(R.id.phone_field))
                .perform(replaceText("(555) 123-4567"), closeSoftKeyboard());
        onView(withId(R.id.phone_field))
            .check(matches(withText("(555) 123-4567")));
        //pick a gender
        onView(withId(R.id.gender_field)).perform(click());
        onData(hasToString(startsWith("Male")))
                .perform(click());
        onView(withId(R.id.gender_field)).check(matches(withSpinnerText(containsString("Male"))));
        //enter a car capacity
        onView(withId(R.id.car_capacity_field))
                .perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.car_capacity_field))
                .check(matches(withText("5")));
        //choose a direction
        onView(withId(R.id.round_trip))
                .perform(click());
        onView(withId(R.id.round_trip))
                .check(matches(isChecked()));

        //scroll
        onView(withId(R.id.input_layout_radius))
                .perform(new NCVScrollToAction());

        //enter date
        onView(withId(R.id.date_field))
                .perform(click());

        onView(withId(com.wdullaer.materialdatetimepicker.R.id.ok))
                .perform(click());
        onView(withId(R.id.date_field))
                .check(matches(withText("Oct 16, 2016")));
        //enter time
        onView(withId(R.id.time_field))
                .perform(click());
        onView(withId(com.wdullaer.materialdatetimepicker.R.id.ok))
                .perform(click());
        onView(withId(R.id.time_field))
                .check(matches(withText("7:00 PM")));
        //enter a location
        onView(withId(R.id.place_autocomplete_fragment))
                .perform(click());
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.findObject(new UiSelector().focused(true)).setText("South Higuera St San Luis Obispo");
        device.findObject(new UiSelector().textContains("South Higuera Street")).click();
        onView(withId(R.id.place_autocomplete_search_input))
                .check(matches(withText("South Higuera Street")));
        //enter a radius
        onView(withId(R.id.radius_field))
                .perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.radius_field))
                .check(matches(withText("1")));
        //click fab
        onView(withId(R.id.fab))
                .perform(click());
        //verify that on the MyRides section meaning the info was valid
//        matchToolbarTitle(InstrumentationRegistry.getTargetContext().getString(R.string.nav_my_rides));
    }

    @Test
    public void testingDisplayRideData() {
        server.setDispatcher(getNewDispatcher());
        switchToMyRides();

        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.editOffering)));

        //check the initial values
        onView(withId(R.id.name_field))
                .check(matches(withText("Terbius Blush")));
        onView(withId(R.id.phone_field))
                .check(matches(withText("(408) 207-3818")));
        onView(withId(R.id.gender_view))
                .check(matches(withText("Male")));
        onView(withId(R.id.car_capacity_field))
                .check(matches(withText("5")));
        onView(withId(R.id.round_trip))
                .check(matches(isChecked()));
        //scroll
        onView(withId(R.id.input_layout_radius))
                .perform(new NCVScrollToAction());
        onView(withId(R.id.date_field))
                .check(matches(withText("2016-10-16")));
        onView(withId(R.id.time_field))
                .check(matches(withText("19:00:00")));
        onView(withId(R.id.place_autocomplete_search_input))
                .check(matches(withText("S Higuera St San Luis Obispo, null, null, USA")));
        onView(withId(R.id.radius_field))
                .check(matches(withText("1.0")));
    }

    @Test
    public void testingEditRideData() throws UiObjectNotFoundException {
        server.setDispatcher(getNewDispatcher());
        switchToMyRides();

        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.editOffering)));

        //edit some fields
        onView(withId(R.id.name_field))
                .perform(replaceText("Terbius Blush II"), closeSoftKeyboard());
        onView(withId(R.id.name_field))
                .check(matches(withText("Terbius Blush II")));
        onView(withId(R.id.car_capacity_field))
                .perform(replaceText("8"), closeSoftKeyboard());
        onView(withId(R.id.car_capacity_field))
                .check(matches(withText("8")));

        //scroll
        onView(withId(R.id.input_layout_radius))
                .perform(new NCVScrollToAction());

//        //enter date
//        onView(withId(R.id.date_field))
//                .perform(click());
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.findObject(new UiSelector().textContains("15")).click();
//
//        onView(withId(com.wdullaer.materialdatetimepicker.R.id.ok))
//                .perform(click());
//        onView(withId(R.id.date_field))
//                .check(matches(withText("Oct 15, 2016")));
//        //enter time
//        onView(withId(R.id.time_field))
//                .perform(click());
//        device.findObject(new UiSelector().textContains("6")).click();
//
//        onView(withId(com.wdullaer.materialdatetimepicker.R.id.ok))
//                .perform(click());
//        onView(withId(R.id.time_field))
//                .check(matches(withText("6:00 PM")));

        onView(withId(R.id.radius_field))
                .perform(replaceText("3"), closeSoftKeyboard());
        onView(withId(R.id.radius_field))
                .check(matches(withText("3")));

        //verify back in main screen
    }
}
