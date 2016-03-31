package org.androidcru.crucentralcoast.presentation.views.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import org.androidcru.crucentralcoast.R;

import butterknife.ButterKnife;

/**
 * Reusable class for Fragments with just a RecyclerView and emptyView for when that RecyclerView
 * is empty.
 *
 * Takes care of inflating a ViewStub when the time is right as well as a SwipeRefreshLayout workaround (see below)
 */
public class ListFragment extends BaseSupportFragment
{
    //Inject views
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private ViewStub emptyViewStub;
    protected View emptyView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        recyclerView.setHasFixedSize(true);

        setupSwipeRefreshLayout(swipeRefreshLayout);
    }

    public static void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        //issue 77712, workaround until Google fixes it
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
    }

    protected void inflateEmptyView(int layoutId)
    {
        if(emptyViewStub == null)
            emptyViewStub = (ViewStub) getView().findViewById(R.id.empty_view_stub);

        if(emptyViewStub != null)
        {
            emptyViewStub.setLayoutResource(layoutId);
            emptyView = emptyViewStub.inflate();
            ButterKnife.bind(this, getView());
        }
    }
}
