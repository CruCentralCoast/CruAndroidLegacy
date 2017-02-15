package org.androidcru.crucentralcoast.tests;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.data.mocking.ServerInstrumentationTest;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;
import org.junit.Rule;
import org.junit.Test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static org.androidcru.crucentralcoast.common.RecyclerViewUtils.withRecyclerView;
import static org.hamcrest.core.IsNot.not;

public class SubscriptionsTests extends ServerInstrumentationTest
{
    @Rule
    public ActivityTestRule<SubscriptionActivity> activityRule = new ActivityTestRule<SubscriptionActivity>(SubscriptionActivity.class, true, false);

    private Dispatcher getNewDispatcher() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                System.out.println(request.getPath() + " " + request.getMethod());

                switch (request.getPath())
                {
                    case "/api/ministries/":
                        return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "ministries.json"));
                    case "/api/campuses/":
                        return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "campuses.json"));
                    default:
                        return new MockResponse().setResponseCode(404);
                }
            }
        };
    }

    @Test
    public void testToggleMinistry()
    {
        server.setDispatcher(getNewDispatcher());

        activityRule.launchActivity(new Intent());

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
        onView(withRecyclerView(R.id.subscription_list)
                .atPositionOnView(1, R.id.checkbox))
                .perform(click());

        // asserts that the checkbox is in the opposite isChecked of where it started
        onView(withRecyclerView(R.id.subscription_list)
                .atPositionOnView(1, R.id.checkbox))
                .check(matches(isChecked ? not(isChecked()) : isChecked()));
    }
}
