package com.androidcru.helloworld;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Activity mActivity;

    public MainActivityInstrumentationTest()
    {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testHelloWorldTV()
    {
        onView(withId(R.id.helloWorldTV))
                .check(matches(withText("Hello World!")));
    }
}
