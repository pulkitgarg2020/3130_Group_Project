package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.Registration.LoginPage;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {
    LoginPage loginPage = new LoginPage();

    @Test
    public void isUserNameEmpty_test() {

        assertTrue(loginPage.isUserNameEmpty(""));
        assertFalse(loginPage.isUserNameEmpty("userName"));
    }
    @Test
    public void isInfoMatch_test(){

        assertTrue(loginPage.isInfoMatch("userName", "password_123456", "userName", "password_123456"));
        assertFalse(loginPage.isInfoMatch("userName", "password_123456", "user", "password_123456"));
        assertFalse(loginPage.isInfoMatch("userName", "password_123456", "userName", "123456"));
        assertFalse(loginPage.isInfoMatch("userName", "password_123456", "user", "123456"));
    }

    @Test
    public void isPasswordEmpty_test(){

        assertTrue(loginPage.isPasswordEmpty(""));
        assertFalse(loginPage.isPasswordEmpty("password_123456"));
    }


}