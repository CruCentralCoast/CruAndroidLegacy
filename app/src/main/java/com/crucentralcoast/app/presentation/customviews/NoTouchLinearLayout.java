package com.crucentralcoast.app.presentation.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class NoTouchLinearLayout extends LinearLayout
{

    public NoTouchLinearLayout(Context context)
    {
        super(context);
    }

    public NoTouchLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NoTouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        super.onInterceptTouchEvent(ev);
        return true;
    }
}
