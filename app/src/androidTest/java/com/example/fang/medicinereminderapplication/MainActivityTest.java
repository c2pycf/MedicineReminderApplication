package com.example.fang.medicinereminderapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<AlarmMainActivity> mActivityTestRule = new ActivityTestRule<>(AlarmMainActivity.class);
    @Before
    public void launchActivity(){

    }

    @Test
    public void NewInstalled(){
        onView(withId(R.id.add_alarm)).check(matches(isDisplayed()));
    }
}
