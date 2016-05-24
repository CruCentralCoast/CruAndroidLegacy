package org.androidcru.crucentralcoast.presentation.views.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ScrollView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.observer.CruObserver;
import org.androidcru.crucentralcoast.data.providers.observer.ObserverUtil;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.Observers;
import timber.log.Timber;

public class ListHelperImpl implements ListHelper
{
    //Inject views
    public RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ViewStub emptyViewStub;
    private ScrollView emptyViewScrollView;

    private ViewStub noNetworkViewStub;
    private ScrollView noNetworkViewScrollView;

    private View noNetworkView;
    private View emptyView;

    private ListHelper child;

    public ListHelperImpl(ListHelper child)
    {
        this.child = child;
    }

    public void onViewCreated(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        emptyViewStub = (ViewStub) view.findViewById(R.id.empty_view_stub);
        noNetworkViewStub = (ViewStub) view.findViewById(R.id.no_network_view_stub);

        emptyViewScrollView = (ScrollView) view.findViewById(R.id.empty_view);
        noNetworkViewScrollView = (ScrollView) view.findViewById(R.id.no_network_view);
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
            noNetworkViewScrollView.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
        emptyViewScrollView.setVisibility(View.VISIBLE);
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
            emptyViewScrollView.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
        noNetworkViewScrollView.setVisibility(View.VISIBLE);
    }

    public void showContent()
    {
        if(emptyView != null)
            emptyViewScrollView.setVisibility(View.GONE);
        if(noNetworkView != null)
            noNetworkViewScrollView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public <T> CruObserver<T> createListObserver(int emptyLayoutId, Action1<T> onNext)
    {
        return ObserverUtil.create(Observers.create(t -> {
                    onNext.call(t);
                    child.showContent();
                },
                e -> Timber.e(e, "Failed to retrieve."),
                () -> swipeRefreshLayout.setRefreshing(false)),
                () -> {
                    child.onEmpty(emptyLayoutId);
                },
                () -> child.onNoNetwork());
    }

    public <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty)
    {
        return ObserverUtil.create(Observers.create(t -> {
                    onNext.call(t);
                    child.showContent();
                },
                e -> Timber.e(e, "Failed to retrieve."),
                () -> swipeRefreshLayout.setRefreshing(false)),
                () -> {
                    onEmpty.call();
                },
                () -> child.onNoNetwork());
    }

    @Override
    public <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty, Action0 onNoNetwork)
    {
        return ObserverUtil.create(Observers.create(t -> {
                    onNext.call(t);
                    child.showContent();
                },
                e -> Timber.e(e, "Failed to retrieve."),
                () -> swipeRefreshLayout.setRefreshing(false)),
                () -> {
                    onEmpty.call();
                },
                () -> onNoNetwork.call());
    }

    public void inflateEmptyView(View v, int layoutId)
    {
        if(emptyViewStub == null)
            emptyViewStub = (ViewStub) v.findViewById(R.id.empty_view_stub);

        if(emptyViewStub != null)
        {
            emptyViewStub.setLayoutResource(layoutId);
            emptyView = emptyViewStub.inflate();
        }
    }
}
