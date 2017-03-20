package com.crucentralcoast.app.tests;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.common.RecyclerViewUtils;
import com.crucentralcoast.app.presentation.views.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MyRidesTests
{
    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private void navigateToMyRides()
    {
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_my_rides)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void testMyRidesDriverRedirectionToEvents()
    {
        navigateToMyRides();

        onView(withId(R.id.events_button_driver)).perform(click());

        //get view reference to first item and assert it's displayed
        onView(RecyclerViewUtils.withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.event_banner))
                .check(matches(isDisplayed()));

    }
}
