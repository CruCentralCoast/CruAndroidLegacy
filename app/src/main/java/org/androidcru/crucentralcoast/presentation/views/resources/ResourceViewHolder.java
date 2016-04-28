package org.androidcru.crucentralcoast.presentation.views.resources;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.customtabs.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.webview.WebviewFallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    @Bind(R.id.title) TextView title;
    @Bind(R.id.tags) TextView tags;
    @Bind(R.id.resource_icon) ImageView typeIcon;

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
