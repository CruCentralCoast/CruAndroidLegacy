package org.androidcru.crucentralcoast.presentation.views.forms;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

public abstract class FormContentFragment extends BaseSupportFragment implements FormContent
{
    protected FormHolder formHolder;
    private Animation.AnimationListener animListener;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        if (nextAnim != 0) {
            Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            if(animListener != null)
            {
                anim.setAnimationListener(animListener);
            }
            return anim;
        } else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

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

    public void setAnimationListener(Animation.AnimationListener animListener)
    {
        this.animListener = animListener;
    }
}
