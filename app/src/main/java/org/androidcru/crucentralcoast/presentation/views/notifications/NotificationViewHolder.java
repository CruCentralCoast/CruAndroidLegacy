package org.androidcru.crucentralcoast.presentation.views.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Notification;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.views.conttactcards.UserContactCardVH;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;
    @BindView(R.id.icon) ImageView icon;
    @BindView(R.id.content_parent) LinearLayout payload;

    private Context context;

    public NotificationViewHolder(View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    public void bind(Notification notification)
    {
        title.setText(notification.content);
        subtitle.setText(DateUtils.getRelativeTimeSpanString(notification.timestamp.toInstant().toEpochMilli()));
        icon.setImageDrawable(DrawableUtil.getDrawable(context, R.drawable.bell));

        if(notification.payload != null && notification.payload.name != null && notification.payload.phone != null)
        {
            payload.setVisibility(View.VISIBLE);
            UserContactCardVH vh = new UserContactCardVH(payload);
            vh.bind(notification.payload);
        }
        else
            payload.setVisibility(View.GONE);
    }
}
