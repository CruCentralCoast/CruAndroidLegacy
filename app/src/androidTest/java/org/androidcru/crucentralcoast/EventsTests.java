package org.androidcru.crucentralcoast;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.androidcru.crucentralcoast.common.RxIdlingResource;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.androidcru.crucentralcoast.common.RecyclerViewUtils.withRecyclerView;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class EventsTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    //call this so that Espresso will wait for network requests to complete before asserting UI stuff
    @BeforeClass
         public static void setup()
    {
        Espresso.registerIdlingResources(RxIdlingResource.get());
    }

    private void switchEvents()
    {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_events)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void doNothing() {
        switchEvents();

        //click on first item
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //get view reference to first item and assert it's displayed
        onView(withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.eventDescription))
                .check(matches(isDisplayed()));

        //click on first item again
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //get view reference to first item and assert it's not displayed
        onView(withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.eventDescription))
                .check(matches(not(isDisplayed())));
    }
}
