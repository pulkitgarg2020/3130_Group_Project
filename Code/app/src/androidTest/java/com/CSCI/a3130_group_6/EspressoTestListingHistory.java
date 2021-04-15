package com.CSCI.a3130_group_6;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.EmployerPackage.EmployerHomepage;
import com.CSCI.a3130_group_6.Listings.ListingHistory;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EspressoTestListingHistory {
    @Rule
    public ActivityScenarioRule<ListingHistory> ListingHistoryRule = new ActivityScenarioRule<>(ListingHistory.class);

    @BeforeClass
    public static void setup(){
        Intents.init();
    }

    /** AT-1**/
    // both above are implemented in nav bar
    /** AT-2**/
    @Test
    public void checkVisibilityNoListing(){
        // should be visible as test situation doesn't contain login information
        onView(withId(R.id.noListingMessage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
    /** AT-3**/
    @Test
    public void checkBackButton(){
        // back button == nav bar
        //onView(withId(R.id.backButton)).perform(click());
        // intended route to EmployerProfile - not yet complete -Bryson :(
        intended(hasComponent(EmployerHomepage.class.getName()));
    }

}
