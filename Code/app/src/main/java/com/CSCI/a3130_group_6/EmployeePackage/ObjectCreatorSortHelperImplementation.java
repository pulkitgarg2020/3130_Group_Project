package com.CSCI.a3130_group_6.EmployeePackage;

import com.CSCI.a3130_group_6.HelperClases.SortHelper;

public class ObjectCreatorSortHelperImplementation implements ObjectCreatorSortHelperSingleton{
    SortHelper sortHelper = null;

    @Override
    public SortHelper getSortHelper() {
        return new SortHelper();
    }
}
