package org.androidcru.crucentralcoast.presentation.views.resources;


import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return new ArticleViewHolder(inflater.inflate(R.layout.item_article, parent, false));
    }

    public void onBindViewHolder(ArticleViewHolder holder, int position)
    {
        Resource curResource = resources.get(position);
        holder.title.setText(curResource.title);
        holder.tags.setText(activity.getString(R.string.tags, curResource.formatTags()));
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

    @Override
    public int getItemCount()
    {
        return resources.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.tags) TextView tags;
        View rootView;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            ButterKnife.bind(this, rootView);
        }
    }
}
