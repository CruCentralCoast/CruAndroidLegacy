package org.androidcru.crucentralcoast;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventsTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

    //suppose to tell Espresso when RxJava finishes a task but it just times out
    //TODO @jon
    /*@BeforeClass
    public static void setup()
    {
        Espresso.registerIdlingResources(new BetterIdlingResource());
    }*/

    private void switchEvents()
    {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.nav_events)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    @Test
    public void doNothing() {
        switchEvents();
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
