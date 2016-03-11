package org.androidcru.crucentralcoast;


import android.content.SharedPreferences;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.androidcru.crucentralcoast.common.RxIdlingResource;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.androidcru.crucentralcoast.common.RecyclerViewUtils.withRecyclerView;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class SubscriptionsTests
{
    @Rule
    public ActivityTestRule<SubscriptionActivity> activityRule = new ActivityTestRule<SubscriptionActivity>(SubscriptionActivity.class);

    //call this so that Espresso will wait for network requests to complete before asserting UI stuff
    @BeforeClass
    public static void setup()
    {
        Espresso.registerIdlingResources(RxIdlingResource.get());
    }

    @Test
    public void testToggleMinistry()
    {
        Boolean isChecked = false;
        try
        {
            onView(withRecyclerView(R.id.subscription_list)
                .atPositionOnView(1, R.id.checkbox))
                .check(matches(isChecked()));
            // if it doesn't fail, the box is now checked
            isChecked = true;
        }
        catch (Exception | AssertionFailedError e)
        {
        }
        //click on first item (0th item is the header)
        onView(withId(R.id.subscription_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // asserts that the checkbox is in the opposite state of where it started
        onView(withRecyclerView(R.id.subscription_list)
                .atPositionOnView(1, R.id.checkbox))
                .check(matches(isChecked ? not(isChecked()) : isChecked()));
    }
}
