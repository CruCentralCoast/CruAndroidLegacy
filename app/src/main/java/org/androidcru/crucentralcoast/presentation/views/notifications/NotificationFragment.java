package org.androidcru.crucentralcoast.presentation.views.notifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Notification;
import org.androidcru.crucentralcoast.data.providers.NotificationProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class NotificationFragment extends ListFragment
{
    private Observer<List<Notification>> observer;
    @BindView(R.id.informational_text) TextView informationalText;


    public NotificationFragment()
    {
        observer = createListObserver(R.layout.empty_with_alert,
                notifications -> {
                    setupList(notifications);
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        informationalText.setText("No notifications at this time!");
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        helper.swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        forceUpdate();
    }

    private void forceUpdate()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        NotificationProvider.getNotifications(this, observer);
    }

    private void setupList(List<Notification> notifications)
    {
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        helper.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helper.recyclerView.setAdapter(adapter);
    }
}

