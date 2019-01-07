package com.example.fang.medicinereminderapplication;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditAlarmActivityTest {

    @Rule
    public ActivityTestRule<EditAlarmActivity> mActivityTestRule = new ActivityTestRule<>(EditAlarmActivity.class);

    @Test
    public void occurrenceOnceTest(){
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Once")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.bt_data_picker)).check(matches(isDisplayed())).perform(click());


    }
}
