package com.CSCI.a3130_group_6.Registration;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;

public class ObjectCreatorEmployerImplementation implements ObjectCreatorEmployerSingleton{
    Employer employer = null;

    @Override
    public Employer getEmployer() {
        return new Employer();
    }
}
