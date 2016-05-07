package org.androidcru.crucentralcoast.presentation.views.forms;

import android.content.Context;

import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

public abstract class FormContentFragment extends BaseSupportFragment implements FormContent
{
    private FormHolder formHolder;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        formHolder = (FormActivity) context;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((FragmentViewListener) formHolder).onFragmentViewInstantiated(this);
    }

    @Override
    public void onNext(FormHolder formHolder)
    {
        formHolder.next();
    }

    @Override
    public void onPrevious(FormHolder formHolder)
    {
        formHolder.prev();
    }
}
