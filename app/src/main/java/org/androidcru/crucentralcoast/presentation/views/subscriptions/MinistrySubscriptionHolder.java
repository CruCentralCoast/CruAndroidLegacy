package org.androidcru.crucentralcoast.presentation.views.subscriptions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

public class MinistrySubscriptionHolder extends RecyclerView.ViewHolder
{
    private static final String HTTPS = "https:";
    protected @BindView(R.id.ministry_image) ImageView ministryImage;
    protected @BindView(R.id.checkbox) CheckBox checkBox;

    private RecyclerView.Adapter adapter;
    protected MinistrySubscription model;
    private Context context;

    private Observer<Boolean> observer = Observers.create((status) -> {
    }, e -> {
        Timber.e(e, "Error when subscribing");
        SharedPreferencesUtil.writeMinistrySubscriptionIsSubscribed(model.subscriptionId, !getIsSubscribed());
    }, () -> {
        bindUI(model);
    });

    public MinistrySubscriptionHolder(View itemView, RecyclerView.Adapter adapter)
    {
        super(itemView);
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        this.adapter =  adapter;
    }

    public void bindUI(MinistrySubscription model)
    {
        this.model = model;
        boolean isChecked = getIsSubscribed();
        checkBox.setChecked(isChecked);
        if(model.image != null && !model.image.isEmpty())
        {
            Context context = ministryImage.getContext();
            // TODO remove check for https prefix once the server is updated
            RequestCreator creator = Picasso.with(context).load(model.image.contains(HTTPS) ?
                    model.image : HTTPS + model.image);
            if (!isChecked) {
                creator = creator.transform(new ColorFilterTransformation(
                        ContextCompat.getColor(context, R.color.cruGray)));
            }
            creator.into(ministryImage);
        }
        else
        {
            ministryImage.setImageResource(R.drawable.default_box);
        }

    }

    public boolean getIsSubscribed()
    {
        return SharedPreferencesUtil.getMinistrySubscriptionIsSubscribed(model.subscriptionId);
    }

    public void setIsSubscribed(boolean isSubscribed)
    {
        SharedPreferencesUtil.writeMinistrySubscriptionIsSubscribed(model.subscriptionId, isSubscribed);
        if(isSubscribed)
            RegistrationIntentService.subscribeToMinistry(observer, context, model.subscriptionId);
        else
            RegistrationIntentService.unsubscribeToMinistry(observer, context, model.subscriptionId);
    }

    @OnClick(R.id.tile_subscription)
    public void onClick(View v)
    {
        setIsSubscribed(!getIsSubscribed());
        adapter.notifyItemChanged(getAdapterPosition());
    }
}
