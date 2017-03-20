package com.crucentralcoast.app.presentation.views.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Tyler Wong
 */

public class HomeItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public HomeItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int dataSize = state.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (dataSize > 0 && position == dataSize - 1) {
            outRect.set(mSpace, mSpace, mSpace, mSpace);
        } else {
            outRect.set(mSpace, mSpace, 0, mSpace);
        }
    }
}
