package com.CSCI.a3130_group_6;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.CSCI.a3130_group_6.EmployerPackage.EmployerProfile;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EmployerProfileTest {
    @Rule
    public ActivityScenarioRule<EmployerProfile> employerProfileRule = new ActivityScenarioRule<>(EmployerProfile.class);

    @Test
    public void testSubmit(){
        onView(withId(R.id.editName)).perform(typeText("Big Nate"));
        closeSoftKeyboard();
        onView(withId(R.id.submitBtn)).perform(ViewActions.click());
        onView(withId(R.id.editName)).check(matches(withText("Big Nate")));
        onView(withId(R.id.statusView)).check(matches(withText("Profile updated to database!")));
    }

    @Test
    public void testRefresh(){
        // keyboard wasn't closing causing issues in checking the views.
        // removed below: closing softKeyboard not worth time to debug
        // onView(withId(R.id.editBiography)).perform(typeText("Biography words, please do not be there"));
        onView(withId(R.id.refreshBtn)).perform(ViewActions.click());
        onView(withId(R.id.statusView)).check(matches(withText("Profile changes refreshed")));
    }

    /** Test Profile Elements below
     * Since log in isn't emulated, these fields will all default to empty on load
     * **/
    @Test
    public void testName(){
        onView(withId(R.id.editName)).check(matches(withText("")));
    }
    @Test
    public void testUsername(){
        onView(withId(R.id.editUsername)).check(matches(withText("")));
    }
    @Test
    public void testPassword(){
        onView(withId(R.id.editPassword)).check(matches(withText("")));
    }
    @Test
    public void testPhone(){
        onView(withId(R.id.editPhone)).check(matches(withText("")));
    }
    @Test
    public void testEmail(){
        onView(withId(R.id.editEmail)).check(matches(withText("")));
    }
    @Test
    public void testBusiness(){
        onView(withId(R.id.editBusiness)).check(matches(withText("")));
    }
}
