package org.androidcru.crucentralcoast.presentation.views.resources;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.customtabs.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.webview.WebviewFallback;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    @BindView(R.id.resource_card_view) CardView cardView;
    @BindView(R.id.resource_divider) View divider;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.tags) TextView tags;
    @BindView(R.id.resource_icon) ImageView typeIcon;

    private Resource model;


    public ResourceViewHolder(View rootView, Boolean isFeed)
    {
        this(rootView);

        if (isFeed)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int margin = DisplayMetricsUtil.dpToPx(rootView.getContext(), 16);
            params.setMargins(0, margin, 0, 0);
            cardView.setLayoutParams(params);
            divider.setVisibility(View.GONE);
        }
        else
        {
            divider.setVisibility(View.VISIBLE);
        }
    }

    public ResourceViewHolder(View rootView) {
        super(rootView);
        rootView.setOnClickListener(this);
        ButterKnife.bind(this, rootView);
    }

    public void bind(Resource resource)
    {
        this.model = resource;
        bindUI();
    }

    private void bindUI()
    {
        title.setText(model.title);
        tags.setText(CruApplication.getContext().getString(R.string.tags, model.formatTags()));
        typeIcon.setImageResource(getResourceIconFromType(model.resourceType));
    }

    private int getResourceIconFromType(Resource.ResourceType type)
    {
        switch(type)
        {
            case ARTICLE:
                return R.drawable.ic_note_outline_grey600;
            case AUDIO:
                return R.drawable.ic_headphones_grey600;
            case VIDEO:
                return R.drawable.ic_video_collection_grey600;
        }

        return -1;
    }

    @Override
    public void onClick(View v)
    {
        CustomTabActivityHelper.openCustomTab(
                (Activity) v.getContext(), ViewUtil.getCustomTabsIntent(v.getContext()), Uri.parse(model.url), new WebviewFallback());
    }
}
