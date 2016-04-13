package org.androidcru.crucentralcoast.presentation.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class NoTouchRelativeLayout extends RelativeLayout
{

    public NoTouchRelativeLayout(Context context)
    {
        super(context);
    }

    public NoTouchRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NoTouchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public NoTouchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        super.onInterceptTouchEvent(ev);
        return true;
    }
}
