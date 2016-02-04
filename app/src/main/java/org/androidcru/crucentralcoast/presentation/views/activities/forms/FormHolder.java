package org.androidcru.crucentralcoast.presentation.views.activities.forms;

public interface FormHolder
{
    void setTitle(String title);
    void setPreviousVisibility(int visibility);
    void setNextVisibility(int visibility);
    void setToolbarExpansion(boolean expanded);
    void complete();
    void setNavigationVisbility(int visibility);
}
