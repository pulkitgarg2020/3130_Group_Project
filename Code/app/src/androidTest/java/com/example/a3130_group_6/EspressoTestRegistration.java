package com.example.a3130_group_6;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EspressoTestRegistration {
    @Rule
    public ActivityScenarioRule<registrationHome> RuleRegistration = new ActivityScenarioRule<>(registrationHome.class);

    @Test
    public void checkIfRegistrationHomeShown() {
        onView(withId(R.id.employer)).check(matches(withText("Are you an employer?")));
        onView(withId(R.id.employeeBtn)).check(matches(withText("Are you an employee?")));
        onView(withId(R.id.back)).check(matches(withText("Back")));
    }
}