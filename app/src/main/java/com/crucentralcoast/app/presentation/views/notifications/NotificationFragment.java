package com.crucentralcoast.app.presentation.views.notifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Notification;
import com.crucentralcoast.app.data.providers.NotificationProvider;
import com.crucentralcoast.app.presentation.util.DividerItemDecoration;
import com.crucentralcoast.app.presentation.views.base.ListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class NotificationFragment extends ListFragment {
    @BindView(R.id.informational_text)
    TextView informationalText;

    private Observer<List<Notification>> observer;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    public NotificationFragment() {
        observer = createListObserver(R.layout.empty_with_alert,
                notifications -> {
                    setupList(notifications);
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        informationalText.setText("No notifications at this time!");
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        helper.swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate());
    }

    @Override
    public void onResume() {
        super.onResume();
        forceUpdate();
    }

    private void forceUpdate() {
        helper.swipeRefreshLayout.setRefreshing(true);
        NotificationProvider.getNotifications(this, observer);
    }

    private void setupList(List<Notification> notifications) {
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        helper.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helper.recyclerView.setAdapter(adapter);
    }
}

