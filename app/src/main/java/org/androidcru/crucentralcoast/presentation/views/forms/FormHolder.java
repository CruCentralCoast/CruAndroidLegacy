package org.androidcru.crucentralcoast.presentation.views.forms;

public interface FormHolder
{
    void setAdapter(FormAdapter adapter);
    void clearUI();
    void setTitle(String title);
    void setPreviousVisibility(int visibility);
    void setNextVisibility(int visibility);
    void setToolbarExpansion(boolean expanded);
    void complete();
    void setNavigationVisbility(int visibility);
    void next();
    void prev();
}
