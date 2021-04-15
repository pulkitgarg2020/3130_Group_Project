package com.CSCI.a3130_group_6.EmployeePackage;

import com.CSCI.a3130_group_6.HelperClases.SortHelper;

public interface ObjectCreatorSortHelperSingleton {
    SortHelper sortHelper = null;
    public SortHelper getSortHelper();
}
