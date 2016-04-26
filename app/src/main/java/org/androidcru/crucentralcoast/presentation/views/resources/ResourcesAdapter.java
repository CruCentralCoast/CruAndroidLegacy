package org.androidcru.crucentralcoast.presentation.views.resources;


import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.util.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.views.webview.WebviewFallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ArticleViewHolder>
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
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArticleViewHolder(inflater.inflate(R.layout.item_resource, parent, false));
    }

    public void onBindViewHolder(ArticleViewHolder holder, int position)
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
                return R.drawable.ic_note_outline_grey600;
            case AUDIO:
                return R.drawable.ic_headphones_grey600;
            case VIDEO:
                return R.drawable.ic_video_collection_grey600;
        }

        return -1;
    }

    @Override
    public int getItemCount()
    {
        return resources.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.tags) TextView tags;
        @Bind(R.id.resource_icon) ImageView typeIcon;
        View rootView;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            ButterKnife.bind(this, rootView);
        }
    }
}
