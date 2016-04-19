package org.androidcru.crucentralcoast.presentation.views.resources;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResourceViewHolder extends RecyclerView.ViewHolder
{
    @Bind(R.id.title) TextView title;
    @Bind(R.id.tags) TextView tags;
    @Bind(R.id.resource_icon) ImageView typeIcon;

    View rootView;

    public ResourceViewHolder(View rootView) {
        super(rootView);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
    }
}
