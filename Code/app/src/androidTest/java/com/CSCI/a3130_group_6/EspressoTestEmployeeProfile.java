package com.CSCI.a3130_group_6;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeProfile;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EspressoTestEmployeeProfile {

    @Rule
    public ActivityScenarioRule<EmployeeProfile> employeeRule = new ActivityScenarioRule<>(EmployeeProfile.class);

    @Test
    public void checkIfNameIsEmpty() {
        onView(withId(R.id.applicantName)).perform(typeText(""));
        onView(withId(R.id.applicantEmail)).perform(typeText("employee@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantPhoneNum)).perform(typeText("1234567"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantMessage)).perform(typeText("test"));
        closeSoftKeyboard();
        onView(withId(R.id.accept)).perform(click());
        onView(withId(R.id.employeeStatusLabel)).check(matches(withText("Error: Please fill in name")));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.applicantName)).perform(typeText("Employee Name"));
        onView(withId(R.id.applicantEmail)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.applicantPhoneNum)).perform(typeText("1234567"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantMessage)).perform(typeText("test"));
        closeSoftKeyboard();
        onView(withId(R.id.accept)).perform(click());
        onView(withId(R.id.employeeStatusLabel)).check(matches(withText("Error: Please fill in email")));
    }

    @Test
    public void checkIfPhoneNumIsEmpty() {
        onView(withId(R.id.applicantName)).perform(typeText("Employee Name"));
        onView(withId(R.id.applicantEmail)).perform(typeText("employee@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantPhoneNum)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.applicantMessage)).perform(typeText("test"));
        closeSoftKeyboard();
        onView(withId(R.id.accept)).perform(click());
        onView(withId(R.id.employeeStatusLabel)).check(matches(withText("Error: Please fill in phone number")));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.applicantName)).perform(typeText("Employee Name"));
        onView(withId(R.id.applicantEmail)).perform(typeText("employee@dal.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantPhoneNum)).perform(typeText("1234567"));
        closeSoftKeyboard();
        onView(withId(R.id.applicantMessage)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.accept)).perform(click());
        onView(withId(R.id.employeeStatusLabel)).check(matches(withText("Error: Please fill in password")));
    }


}

