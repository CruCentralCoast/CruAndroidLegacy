package com.crucentralcoast.app.presentation.viewmodels;

public class ExpandableState<T>
{
    public T model;
    public boolean isExpanded;

    public ExpandableState(T model)
    {
        this.model = model;
        isExpanded = false;
    }
}
