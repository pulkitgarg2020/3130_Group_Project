package com.CSCI.a3130_group_6.Registration;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;

public interface ObjectCreatorEmployerSingleton {
    Employer employer = null;
    public Employer getEmployer();
}
