package org.androidcru.crucentralcoast.presentation.views.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.observer.CruObserver;
import org.androidcru.crucentralcoast.data.providers.observer.ObserverUtil;

import rx.functions.Action1;
import rx.observers.Observers;
import timber.log.Timber;

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

    private ViewStub noNetworkViewStub;
    protected View noNetworkView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        noNetworkViewStub = (ViewStub) getView().findViewById(R.id.no_network_view_stub);
        recyclerView.setHasFixedSize(true);

        setupSwipeRefreshLayout(swipeRefreshLayout);
    }

    public static void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        //issue 77712, workaround until Google fixes it
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
    }

    public View onEmpty(int layoutId)
    {
        if(emptyView == null)
        {
            emptyViewStub.setLayoutResource(layoutId);
            emptyView = emptyViewStub.inflate();
        }

        if(noNetworkView != null)
            noNetworkView.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        return emptyView;
    }

    public void onNoNetwork()
    {
        if(noNetworkView == null)
        {
            noNetworkViewStub.setLayoutResource(R.layout.no_network);
            noNetworkView = noNetworkViewStub.inflate();
        }

        if(emptyView != null)
            emptyView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
        noNetworkView.setVisibility(View.VISIBLE);
    }

    public void showContent()
    {
        if(emptyView != null)
            emptyView.setVisibility(View.GONE);
        if(noNetworkView != null)
            noNetworkView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    protected <T> CruObserver<T> createListObserver(int emptyLayoutId, Action1<T> onNext)
    {
        return ObserverUtil.create(this, Observers.create(onNext,
                e -> Timber.e(e, "Failed to retrieve."),
                () -> swipeRefreshLayout.setRefreshing(false)),
                () -> onEmpty(emptyLayoutId),
                () -> onNoNetwork());
    }

    protected void inflateEmptyView(int layoutId)
    {
        if(emptyViewStub == null)
            emptyViewStub = (ViewStub) getView().findViewById(R.id.empty_view_stub);

        if(emptyViewStub != null)
        {
            emptyViewStub.setLayoutResource(layoutId);
            emptyView = emptyViewStub.inflate();
        }
    }
}
