package com.CSCI.a3130_group_6;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeAcceptedListings;
import com.CSCI.a3130_group_6.EmployeePackage.EmployeeHomepage;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EspressoTestEmployeeHomepage {
    @Rule
    public ActivityScenarioRule<EmployeeHomepage> employeeHomepage = new ActivityScenarioRule<>(EmployeeHomepage.class);

    @BeforeClass
    public static void setup(){
        Intents.init();
    }

    @Test
    public void checkIFMovedToAcceptedListings(){
        onView(withId(R.id.acceptListingsButton)).perform(click());
        intended(hasComponent(EmployeeAcceptedListings.class.getName()));
    }
}
