package org.androidcru.crucentralcoast;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.androidcru.crucentralcoast.common.RecyclerViewUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

import org.androidcru.crucentralcoast.common.RxIdlingResource;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MyRidesTests
{
    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    //call this so that Espresso will wait for network requests to complete before asserting UI stuff
    @BeforeClass
    public static void setup()
    {
        Espresso.registerIdlingResources(RxIdlingResource.get());
    }

    private void navigateToMyRides()
    {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_my_rides)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void testMyRidesDriverRedirectionToEvents()
    {
        navigateToMyRides();

        onView(withId(R.id.events_button_driver)).perform(click());

        //get view reference to first item and assert it's displayed
        onView(withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.event_banner))
                .check(matches(isDisplayed()));

    }
}
