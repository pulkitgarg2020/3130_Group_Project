package com.CSCI.a3130_group_6.EmployeePackage;

public class ObjectCreatorEmployeeImplementation implements ObjectCreatorEmployeeSingleton{
    Employee employee = null;
    @Override
    public Employee getEmployee() {
        return new Employee();
    }
}
