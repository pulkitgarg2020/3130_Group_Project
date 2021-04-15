package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;
import com.CSCI.a3130_group_6.Registration.RegistrationForEmployers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class EmployerUnitTest {
    private final String userName = "userName1";
    private final String passWord = "password1";
    private final String emailAddress = "email@dal.ca";
    private final String phone = "123456";
    private final String name = "People";
    private final String businessName = "boss";
    Employer employer = new Employer(userName, passWord, phone, emailAddress, name, businessName);
    RegistrationForEmployers reg = new RegistrationForEmployers();

    @Test
    public void getUserName_test(){
        assertEquals(userName, employer.getUserName());
        assertNotEquals("userName2",employer.getUserName());
    }

    @Test
    public void getPassword_test(){
        assertEquals(passWord, employer.getPassword());
        assertNotEquals("password2",employer.getPassword());
    }
    @Test
    public void setUserName_test(){
        employer.setUserName("userName2");
        assertEquals("userName2", employer.getUserName());
    }

    @Test
    public void setPassWord_test(){
        employer.setPassword("password2");
        assertEquals("password2", employer.getPassword());
    }

    @Test
    public void setEmailAddress_test(){

    }
    @Test
    public void setPhone_test(){

    }
}
