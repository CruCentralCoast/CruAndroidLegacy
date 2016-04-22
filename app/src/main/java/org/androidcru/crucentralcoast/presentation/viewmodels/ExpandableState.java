package org.androidcru.crucentralcoast.presentation.viewmodels;

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
