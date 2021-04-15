package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.Registration.RegistrationForEmployers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EmployerRegistrationUnitTest {
    RegistrationForEmployers regEmployer = new RegistrationForEmployers();

    @Test
    public void passwordValidation_test(){
        regEmployer.isPasswordMatched("123456789", "123456789");
        assertTrue( regEmployer.isPasswordMatched("123456789", "123456789"));
        assertFalse(regEmployer.isPasswordMatched("123456789", "12345678"));
    }
}
