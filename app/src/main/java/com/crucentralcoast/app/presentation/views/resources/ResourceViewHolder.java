package com.crucentralcoast.app.presentation.views.resources;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Resource;
import com.crucentralcoast.app.presentation.customtabs.CustomTabActivityHelper;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.webview.WebviewFallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    @BindView(R.id.resource_card_view) CardView cardView;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.tags) TextView tags;
    @BindView(R.id.resource_icon) ImageView typeIcon;
    @BindView(R.id.more_action) TextView moreAction;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.resource_image) ImageView resourceImage;

    private Resource model;

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
        tags.setText(model.formatTags());
        typeIcon.setImageResource(getResourceIconFromType(model.resourceType));
        moreAction.setText(getActionTextFromType(model.resourceType));
        author.setText(model.author);
        author.setVisibility(TextUtils.isEmpty(model.author) ? View.GONE : View.VISIBLE);
        description.setText(model.description);
        description.setVisibility(TextUtils.isEmpty(model.description) ? View.GONE : View.VISIBLE);
        ViewUtil.setSource(resourceImage, model.imageLink, 0, null, null, ViewUtil.SCALE_TYPE.FIT);
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

    private String getActionTextFromType(Resource.ResourceType type) {
        switch (type) {
            case ARTICLE:
                return CruApplication.getContext().getString(R.string.read);
            case AUDIO:
                return CruApplication.getContext().getString(R.string.listen);
            case VIDEO:
                return CruApplication.getContext().getString(R.string.watch);
        }
        return null;
    }

    @Override
    public void onClick(View v)
    {
        CustomTabActivityHelper.openCustomTab(
                (Activity) v.getContext(), ViewUtil.getCustomTabsIntent(v.getContext()), Uri.parse(model.url), new WebviewFallback());
    }
}
