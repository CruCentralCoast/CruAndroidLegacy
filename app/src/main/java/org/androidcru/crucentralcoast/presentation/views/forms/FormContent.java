package org.androidcru.crucentralcoast.presentation.views.forms;

public interface FormContent
{
    void onNext();
    void onPrevious();
    void setSuccessor(FormContent formContent);
    FormContent getSuccessor();
}
