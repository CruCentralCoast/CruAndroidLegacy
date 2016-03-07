package org.androidcru.crucentralcoast.presentation.views.forms;

public interface FormHolder
{
    void setAdapter(FormAdapter adapter);
    void clearUI();
    void setTitle(String title);
    void setSubtitle(String title);
    void setPreviousVisibility(int visibility);
    void setNextVisibility(int visibility);
    void setToolbarExpansion(boolean expanded);
    void complete();
    void setNavigationVisibility(int visibility);
    void next();
    void setNextText(String text);
    void prev();
    void setNavigationClickable(boolean isClickable);

    void addDataObject(Object dataObject);
    Object getDataObject();
}
