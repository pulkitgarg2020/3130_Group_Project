package com.CSCI.a3130_group_6;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.CSCI.a3130_group_6.Registration.RegistrationForEmployers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
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

public class EsspressoTestRegistration_Employer {

    @Rule
    public ActivityScenarioRule<RegistrationForEmployers> RuleRegistration = new ActivityScenarioRule<>(RegistrationForEmployers.class);

    @Test
    public void checkIfRegistrationEmployerShows() {
        onView(withId(R.id.Submit1)).check(matches(withText("Submit")));
        onView(withId(R.id.Name)).check(matches(withText("Name")));
        onView(withId(R.id.Username)).check(matches(withText("Username")));
        onView(withId(R.id.Password)).check(matches(withText("Password")));
        onView(withId(R.id.VPassword)).check(matches(withText("Verify Password")));
        onView(withId(R.id.Email)).check(matches(withText("Email")));
        onView(withId(R.id.BName)).check(matches(withText("Business Name")));
        onView(withId(R.id.emp)).check(matches(withText("Employee")));
    }

    @Test
    public void checkIfDuplicateUserName() {
        onView(withId(R.id.username)).perform(typeText("123"));
        closeSoftKeyboard();
        onView(withId(R.id.error)).check(matches(withText("Username already taken. Please enter a different username.")));
    }

    @Test
    public void checkIfValidUserName() {
        onView(withId(R.id.username)).perform(typeText("new_employer"));
        closeSoftKeyboard();
        onView(withId(R.id.error)).check(matches(withText("Username valid")));
    }

    @Test
    public void checkIfUserNameShort() {
        onView(withId(R.id.username)).perform(typeText("a"));
        onView(withId(R.id.error)).check(matches(withText("Username too short")));
    }

    @Test
    public void checkRegistrationEmployeePasswordInvalidation(){
        onView(withId(R.id.name)).perform(typeText("Han"));
        onView(withId(R.id.username)).perform(typeText("abcdef"));
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.vpassword)).perform(typeText("1234"));
        onView(withId(R.id.phone)).perform(typeText("9020008888"));
        onView(withId(R.id.email)).perform(typeText("abcdef@gamil.com"));
        closeSoftKeyboard();
        onView(withId(R.id.Submit1)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("password is not matched")));
    }

}
