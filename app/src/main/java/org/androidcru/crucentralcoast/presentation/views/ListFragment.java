package org.androidcru.crucentralcoast.presentation.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import org.androidcru.crucentralcoast.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Reuseable class for Fragments with just a RecyclerView and emptyView for when that RecyclerView
 * is empty.
 *
 * Takes care of inflating a ViewStub when the time is right as well as a SwipeRefreshLayout workaround (see below)
 */
public class ListFragment extends Fragment
{
    //Inject views
    @Bind(R.id.recyclerview) protected RecyclerView recyclerView;
    @Bind(R.id.event_swipe_refresh_layout) protected SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @Bind(R.id.empty_view_stub) ViewStub emptyViewStub;
    protected View emptyView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);

        setupSwipeRefreshLayout();
    }

    protected void setupSwipeRefreshLayout()
    {
        //issue 77712, workaround until Google fixes it
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);

        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
    }

    protected void inflateEmptyView(int layoutId)
    {
        if(emptyViewStub == null)
            emptyViewStub = (ViewStub) getView().findViewById(R.id.empty_view_stub);

        emptyViewStub.setLayoutResource(layoutId);
        emptyView = emptyViewStub.inflate();
        ButterKnife.bind(this, getView());
    }
}
