package org.androidcru.crucentralcoast.presentation.views.forms;

import android.content.Context;

import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

public abstract class FormContentFragment extends BaseSupportFragment implements FormContent
{
    protected FormHolder formHolder;

    @Override
    public final void onAttach(Context context)
    {
        super.onAttach(context);
        formHolder = (FormHolder) getActivity();
    }

    @Override
    public void onNext()
    {
        formHolder.next();
    }

    @Override
    public void onPrevious()
    {
        formHolder.prev();
    }
}
