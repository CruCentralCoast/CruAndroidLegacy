package com.crucentralcoast.app.tests;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.common.RecyclerViewUtils;
import com.crucentralcoast.app.presentation.views.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class EventsTests extends ServerTest
{
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private void switchEvents()
    {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_events)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void doNothing() {
        String eventsJson = ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "events.json");
        server.enqueue(new MockResponse().setBody(eventsJson));

        switchEvents();

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //get view reference to first item and assert it's displayed
        onView(RecyclerViewUtils.withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.eventDescription))
                .check(matches(isDisplayed()));

        //click on first item again
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //get view reference to first item and assert it's not displayed
        onView(RecyclerViewUtils.withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.eventDescription))
                .check(matches(not(isDisplayed())));
    }
}
