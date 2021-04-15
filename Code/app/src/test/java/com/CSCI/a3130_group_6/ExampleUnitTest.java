package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.EmployerPackage.EmployerHomepage;
import com.CSCI.a3130_group_6.Listings.AddListing;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * Acceptance Tests
 * AT 1: Given that I want to use the program, I expect to see a search bar and button where I can search for available tasks or available employees to do the task
 * AT 2: Given that I click the search button, as an employer I expect to see a list of employees who can do the task and as an employee I expect to see available tasks
 * AT 3: Given that I am on the homepage, I expect to see the app’s banner/logo on the top
 * AT 4: Given that I am on the homepage, I expect to see a home button which refreshes the page
 * AT 5: Given that I am an employee/employer I expect to see available tasks/available taskers in my local
 */
public class ExampleUnitTest {
    static AddListing addListing;
    static EmployerHomepage employerHomepage;

    @BeforeClass
    public static void setup() {
        addListing = new AddListing();
        employerHomepage = new EmployerHomepage();
    }

    @Test
    public void checkIfTaskTitleEmpty() {
        assertTrue(addListing.isEmptyTaskTitle(""));
        assertFalse(addListing.isEmptyTaskTitle("Good Task Title"));
    }


    // search bar test check
    @Test
    public void searchBar(){
        assertFalse((employerHomepage.searchFunctioning("")));
    }
    // search bar typed test check
    @Test
    public void checkIfTaskDescriptionEmpty() {
        assertTrue(addListing.isEmptyTaskDescription(""));
        assertFalse(addListing.isEmptyTaskDescription("Good Task Description"));
    }

    @Test
    public void checkIfUrgencyIsEmpty() {
        assertTrue(addListing.isEmptyUrgency(""));
        assertFalse(addListing.isEmptyUrgency("2"));
    }

    @Test
    public void checkIfUrgencyCorrectRange() {
        assertTrue(addListing.checkUrgencyRange("2"));
        assertFalse(addListing.checkUrgencyRange("6"));
        // add a check or a test for invalid input
    }

    @Test
    public void checkIfDateIsEmpty() {
        assertTrue(addListing.isEmptyDate(""));
        assertFalse(addListing.isEmptyDate("20/02/2020"));
    }

    @Test
    public void checkIfPayIsEmpty() {
        assertTrue(addListing.isEmptyPay(""));
        assertFalse(addListing.isEmptyPay("20"));
    }

    @Test
    public void checkIfLocationEmpty(){
        assertTrue(addListing.isEmptyLocation(""));
        assertFalse(addListing.isEmptyLocation("Halifax"));
    }

    public void searchBarType(){
        assertTrue(employerHomepage.searchFunctioning("Jim"));
    }
    //employee details elements
    @Test
    public void employeeDetails(){
        String[] employees = new String[]{};
        assertFalse(employerHomepage.checkEmployeeList(employees));
    }
    //employee with elements
    @Test
    public void employeeWithDetails(){
        String[] employees = new String[] { "Noback Endintegration", "Potter Weasley", "Henry Harry", "Jim Jones", "Granger Jones Jr."};
        assertTrue(employerHomepage.checkEmployeeList(employees));
    }
}
