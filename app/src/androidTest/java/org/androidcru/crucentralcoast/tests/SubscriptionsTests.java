package org.androidcru.crucentralcoast.tests;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import junit.framework.AssertionFailedError;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.mocking.ResourcesUtil;
import org.androidcru.crucentralcoast.mocking.ServerTest;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.androidcru.crucentralcoast.common.RecyclerViewUtils.withRecyclerView;
import static org.hamcrest.core.IsNot.not;

public class SubscriptionsTests extends ServerTest
{
    @Rule
    public ActivityTestRule<SubscriptionActivity> activityRule = new ActivityTestRule<SubscriptionActivity>(SubscriptionActivity.class);

    private Dispatcher getNewDispatcher() {
        Dispatcher dispatch = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                System.out.println(request.getPath() + " " + request.getMethod());

                if (request.getPath().equals("/api/ministries/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "ministries.json"));
                else if (request.getPath().equals("/api/campuses/"))
                    return new MockResponse().setResponseCode(200).setBody(ResourcesUtil.getResourceAsString(getClass().getClassLoader(), "campuses.json"));
                else
                    return new MockResponse().setResponseCode(404);
            }
        };
        return dispatch;
    }

    @Test
    public void testToggleMinistry()
    {
        server.setDispatcher(getNewDispatcher());

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
