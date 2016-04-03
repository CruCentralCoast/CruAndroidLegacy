package org.androidcru.crucentralcoast.presentation.views.forms;

import java.util.List;

public interface FormHolder
{
    void setFormContent(List<FormContentFragment> fragments);
    void clearUI();
    void setTitle(String title);
    void setSubtitle(String title);
    void setPreviousVisibility(int visibility);
    void setNextVisibility(int visibility);
    void complete();
    void setNavigationVisibility(int visibility);
    void next();
    void setFormState(FormState state);
    void setNextText(String text);
    void prev();
    void setNavigationClickable(boolean isClickable);

    void addDataObject(String key, Object dataObject);
    Object getDataObject(String key);
}
