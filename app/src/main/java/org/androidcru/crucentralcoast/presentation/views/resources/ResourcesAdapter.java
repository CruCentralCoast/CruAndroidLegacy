package org.androidcru.crucentralcoast.presentation.views.resources;


import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.util.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.views.webview.WebviewFallback;

import java.util.ArrayList;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourceViewHolder>
{
    private Activity activity;
    private ArrayList<Resource> resources;
    private CustomTabsIntent.Builder customTabsIntentBuilder;

    public ResourcesAdapter(Activity activity, ArrayList<Resource> resources, CustomTabsIntent.Builder customTabsIntentBuilder)
    {
        this.activity = activity;
        this.resources = resources;
        this.customTabsIntentBuilder = customTabsIntentBuilder;
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ResourceViewHolder(inflater.inflate(R.layout.item_resource, parent, false));
    }

    public void onBindViewHolder(ResourceViewHolder holder, int position)
    {
        Resource curResource = resources.get(position);
        holder.title.setText(curResource.title);
        holder.tags.setText(activity.getString(R.string.tags, curResource.formatTags()));
        holder.typeIcon.setImageResource(getResourceIconFromType(curResource.resourceType));
        holder.rootView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomTabActivityHelper.openCustomTab(
                        activity, customTabsIntentBuilder.build(), Uri.parse(curResource.url), new WebviewFallback());
            }
        });
    }

    private int getResourceIconFromType(Resource.ResourceType type)
    {
        switch(type)
        {
            case ARTICLE:
                return R.drawable.ic_note_outline_grey600_48dp;
            case AUDIO:
                return R.drawable.ic_headphones_grey600_48dp;
            case VIDEO:
                return R.drawable.ic_video_collection;
        }

        return -1;
    }

    @Override
    public int getItemCount()
    {
        return resources.size();
    }
}
