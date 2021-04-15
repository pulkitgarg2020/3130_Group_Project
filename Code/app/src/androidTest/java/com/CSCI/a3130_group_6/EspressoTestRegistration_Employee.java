package com.CSCI.a3130_group_6;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.CSCI.a3130_group_6.Registration.RegistrationForEmployees;

import org.junit.BeforeClass;
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
public class EspressoTestRegistration_Employee {

    @Rule
    public ActivityScenarioRule<RegistrationForEmployees> RuleRegistration = new ActivityScenarioRule<>(RegistrationForEmployees.class);

    @BeforeClass
    public static void setup(){
        Intents.init();
    }


    @Test
    public void checkIfRegistrationEmployeeShows() {
        onView(withId(R.id.Submit1)).check(matches(withText("Submit")));
        onView(withId(R.id.Name)).check(matches(withText("Name")));
        onView(withId(R.id.Username)).check(matches(withText("Username")));
        onView(withId(R.id.Password)).check(matches(withText("Password")));
        onView(withId(R.id.VPassword)).check(matches(withText("Verify Password")));
        onView(withId(R.id.Email)).check(matches(withText("Email")));
    }

    @Test
    public void isPasswordVerified(){
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.vpassword)).perform(typeText("password"));
        onView(withId(R.id.password)).check(matches(withText("password")));
        onView(withId(R.id.vpassword)).check(matches(withText("password")));
    }


    @Test
    public void checkIfUserNameShort() {
        onView(withId(R.id.username)).perform(typeText("a"));
        onView(withId(R.id.employeeUserError)).check(matches(withText("Username too short")));
    }

    /**
     * For the test to pass make sure that user with username Emily exists in the
     * database
     */
    @Test
    public void checkIfDuplicateUserName() {
        onView(withId(R.id.username)).perform(typeText("Emily"));
        closeSoftKeyboard();
        onView(withId(R.id.employeeUserError)).check(matches(withText("Username already taken. Please enter a different username.")));
    }

    @Test
    public void checkIfValidUserName() {
        onView(withId(R.id.username)).perform(typeText("new_worker"));
        closeSoftKeyboard();
        onView(withId(R.id.employeeUserError)).check(matches(withText("Username valid")));
    }

    @Test
    public void checkRegistrationEmployeePasswordInvalidation(){
        onView(withId(R.id.name)).perform(typeText("testing"));
        onView(withId(R.id.username)).perform(typeText("new_worker"));
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.vpassword)).perform(typeText("1234"));
        onView(withId(R.id.phone)).perform(typeText("999000111"));
        onView(withId(R.id.email)).perform(typeText("345@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.Submit1)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("password is not matched")));
        
    }

}

