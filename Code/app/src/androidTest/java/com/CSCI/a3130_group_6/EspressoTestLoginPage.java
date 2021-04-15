package com.CSCI.a3130_group_6;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.CSCI.a3130_group_6.Registration.LoginPage;

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
public class EspressoTestLoginPage {
    @Rule
    public ActivityScenarioRule<LoginPage> loginRule = new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void checkIfLoginPageIsShown() {
        onView(withId(R.id.userNameEt)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.passwordEt)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.loginBt_employee)).check(matches(withText("login as Employee")));
        onView(withId(R.id.registerBt)).check(matches(withText("registration")));
        onView(withId(R.id.loginBt_employer)).check(matches(withText("Login as Employer")));
    }

    @Test
    public void checkIfEmployerUserNameIsEmpty(){
        onView(withId(R.id.userNameEt)).perform(typeText(""));
        onView(withId(R.id.passwordEt)).perform(typeText("Password_123456"));
        onView(withId(R.id.loginBt_employer)).perform(click());
        onView(withId(R.id.loginStatus)).check(matches(withText(R.string.EMPTY_USER_NAME)));
    }

    @Test
    public void checkIfEmployerPasswordIsEmpty(){
        onView(withId(R.id.userNameEt)).perform(typeText("userName"));
        onView(withId(R.id.passwordEt)).perform(typeText(""));
        onView(withId(R.id.loginBt_employer)).perform(click());
        onView(withId(R.id.loginStatus)).check(matches(withText(R.string.EMPTY_PASSWORD)));
    }

    /**
    @Test
    public void checkIfEmployerCanLogin(){
        onView(withId(R.id.userNameEt)).perform(typeText("123"));
        onView(withId(R.id.passwordEt)).perform(typeText("123"));
        onView(withId(R.id.loginBt_employer)).perform(click());
        intended(hasComponent(EmployerHomepage.class.getName()));
    }
    **/
}



