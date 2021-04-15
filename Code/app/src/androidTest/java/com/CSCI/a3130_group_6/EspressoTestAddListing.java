package com.CSCI.a3130_group_6;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.Listings.AddListing;
import com.CSCI.a3130_group_6.Listings.AddListingMap;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EspressoTestAddListing {

    @Rule
    public ActivityScenarioRule<AddListing> employerRule = new ActivityScenarioRule<>(AddListing.class);

    @Rule
    public ActivityScenarioRule<AddListingMap> listingMap = new ActivityScenarioRule<>(AddListingMap.class);

    @BeforeClass
    public static void setup(){
        Intents.init();
    }

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

    @Test
    public void checkIfLocationIsEmpty(){
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        closeSoftKeyboard();
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        closeSoftKeyboard();
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        closeSoftKeyboard();
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.submitTask)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Error: Please choose a location")));
    }

    @Test
    public void checkIfTaskInputsStaySame() throws InterruptedException {
        onView(withId(R.id.titleInput)).perform(typeText("Awesome Task Title"));
        closeSoftKeyboard();
        onView(withId(R.id.descriptionInput)).perform(typeText("Here is a description"));
        closeSoftKeyboard();
        onView(withId(R.id.urgencyInput)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.dateInput)).perform(typeText("20/10/2021"));
        closeSoftKeyboard();
        onView(withId(R.id.payInput)).perform(typeText("20"));
        closeSoftKeyboard();
        onView(withId(R.id.add_locationBt)).perform(click());
        closeSoftKeyboard();
        Thread.sleep(2000);
        onView(withId(R.id.submitButton)).perform(click());
        onView(withId(R.id.titleInput)).check(matches(withText("Awesome Task Title")));
        onView(withId(R.id.descriptionInput)).check(matches(withText("Here is a description")));
        onView(withId(R.id.urgencyInput)).check(matches(withText("1")));
        onView(withId(R.id.dateInput)).check(matches(withText("20/10/2021")));
        onView(withId(R.id.payInput)).check(matches(withText("20")));
    }

    @Test
    public void checkIFMovedToAddListingMap(){
        onView(withId(R.id.add_locationBt)).perform(click());
        intended(hasComponent(AddListingMap.class.getName()));
    }

}

