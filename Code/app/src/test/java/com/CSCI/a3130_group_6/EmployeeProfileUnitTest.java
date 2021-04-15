package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeProfile;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeProfileUnitTest {
    static EmployeeProfile employee;

    @BeforeClass
    public static void setup() {
        employee = new EmployeeProfile();
    }

    @Test
    public void checkIfNameIsEmpty() {
        assertTrue(EmployeeProfile.isNameEmpty(""));
        assertFalse(EmployeeProfile.isNameEmpty("Employee Name"));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(EmployeeProfile.isEmailEmpty(""));
        assertFalse(EmployeeProfile.isEmailEmpty("email@dal.ca"));
    }

    @Test
    public void checkIfPhoneNumberIsEmpty() {
        assertTrue(EmployeeProfile.isPhoneNumEmpty(""));
        assertFalse(EmployeeProfile.isPhoneNumEmpty("1234567"));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        assertTrue(EmployeeProfile.isPasswordEmpty(""));
        assertFalse(EmployeeProfile.isPasswordEmpty("employeePassword"));
    }
}
