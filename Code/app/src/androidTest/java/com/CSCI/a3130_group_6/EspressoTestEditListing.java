package com.CSCI.a3130_group_6;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.Listings.EditEmployerListing;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EspressoTestEditListing {

    @Rule
    public ActivityScenarioRule<EditEmployerListing> employerRule = new ActivityScenarioRule<EditEmployerListing>(EditEmployerListing.class);

    @Test
    public void checkIfTaskTitleEmpty() {
        onView(withId(R.id.titleInput)).perform(typeText(""));
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Empty Task Title")));
    }

    @Test
    public void checkIfTaskDescriptionEmpty() {
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        onView(withId(R.id.descriptionInput)).perform(typeText(""));
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Empty Task Description")));
    }

    @Test
    public void checkIfUrgencyEmpty() {
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        onView(withId(R.id.urgencyInput)).perform(typeText(""));
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Please fill in Urgency")));
    }

    @Test
    public void checkIfDateIsEmpty() {
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        onView(withId(R.id.dateInput)).perform(typeText(""));
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Please fill in Date")));
    }

    @Test
    public void checkIfPayIsEmpty() {
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        onView(withId(R.id.payInput)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Please fill in Pay")));
    }
}


