package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.Registration.RegistrationForEmployees;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeRegistrationUnitTest {
    RegistrationForEmployees regEmployee = new RegistrationForEmployees();

    @Test
    public void passwordValidation_test(){
        regEmployee.isPasswordMatched("123456789", "123456789");
        assertTrue( regEmployee.isPasswordMatched("123456789", "123456789"));
        assertFalse(regEmployee.isPasswordMatched("123456789", "12345678"));
    }

    @Test
    public void checkIfLocationEmpty(){
        assertTrue(regEmployee.isEmptyLocation(""));
        assertFalse(regEmployee.isEmptyLocation("Halifax"));
    }
    @Test
    public void checkIfRadiusNum(){
        assertFalse(regEmployee.validateRadius("1a"));
        assertTrue(regEmployee.validateRadius("15"));

    }
    @Test
    public void checkIfRadiusRange(){
        assertTrue(regEmployee.validateRadiusRange("10"));
        assertFalse(regEmployee.validateRadiusRange("-3"));
        assertFalse(regEmployee.validateRadiusRange("30"));
    }
    @Test
    public void checkIfRadiusEmpty(){
        assertTrue(regEmployee.isEmptyRadius(""));
    }
}
