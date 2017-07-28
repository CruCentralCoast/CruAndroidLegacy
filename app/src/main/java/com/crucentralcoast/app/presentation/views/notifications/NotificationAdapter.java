package com.crucentralcoast.app.presentation.views.notifications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>
{
    private List<Notification> notifications;

    private final int WITHOUT_PAYLOAD = 0;
    private final int WITH_PAYLOAD = 1;


    public NotificationAdapter(List<Notification> notifications)
    {
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new NotificationViewHolder(inflater.inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position)
    {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return notifications.get(position).payload != null ? WITH_PAYLOAD : WITHOUT_PAYLOAD;
    }
}
