package com.CSCI.a3130_group_6;

import com.CSCI.a3130_group_6.EmployeePackage.Employee;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class EmployeeUnitTest {
    private final String userName = "userName1";
    private final String passWord = "password1";
    private final String emailAddress = "email@dal.ca";
    private final String phone = "123456";
    private final String name = "people";
    Employee employee = new Employee(userName, passWord, phone, emailAddress, name);

    @Test
    public void getUserName_test(){
        assertEquals(userName, employee.getUserName());
        assertNotEquals("userName2",employee.getUserName());
    }

    @Test
    public void getPassword_test(){
        assertEquals(passWord, employee.getPassword());
        assertNotEquals("password2",employee.getPassword());
    }

}
