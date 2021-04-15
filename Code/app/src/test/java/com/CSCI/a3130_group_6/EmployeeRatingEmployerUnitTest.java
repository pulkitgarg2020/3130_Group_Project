package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeRatingEmployer;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeRatingEmployerUnitTest {
    static EmployeeRatingEmployer empRating;

    @BeforeClass
    public static void setup() {
        empRating = new EmployeeRatingEmployer();
    }

    @Test
    public void checkIfRatingCorrectRange() {
        assertTrue(empRating.checkRatingRange(2));
        assertFalse(empRating.checkRatingRange(6));
    }

}
