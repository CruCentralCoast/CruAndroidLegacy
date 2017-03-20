package com.crucentralcoast.app.presentation.views.forms;

public interface FormContent
{
    void setupData(FormHolder formHolder);
    void onNext(FormHolder formHolder);
    void onPrevious(FormHolder formHolder);
}
