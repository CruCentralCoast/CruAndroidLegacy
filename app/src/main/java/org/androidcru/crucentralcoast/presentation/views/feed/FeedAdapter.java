package org.androidcru.crucentralcoast.presentation.views.feed;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.data.DatedVideo;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;
import org.androidcru.crucentralcoast.presentation.views.events.CruEventViewHolder;
import org.androidcru.crucentralcoast.presentation.views.resources.ResourceViewHolder;
import org.androidcru.crucentralcoast.presentation.views.videos.CruVideoViewHolder;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<Dateable> items;

    private final int CRU_EVENT = 0;
    private final int YOUTUBE_VIDEO = 1;
    private final int RESOURCE = 2;

    private RecyclerView.LayoutManager layoutManager;

    public FeedAdapter(ArrayList<Dateable> items, RecyclerView.LayoutManager layoutManager)
    {
        this.items = items;
        this.layoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case CRU_EVENT:
                return new CruEventViewHolder(parent, layoutManager, this);
            case YOUTUBE_VIDEO:
                return new CruVideoViewHolder(parent);
            default:
                return new ResourceViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(items.get(position) instanceof CruEventVM)
        {
            return CRU_EVENT;
        }
        if(items.get(position) instanceof DatedVideo)
        {
            return YOUTUBE_VIDEO;
        }
        return RESOURCE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof CruEventViewHolder)
        {

        }
        if(holder instanceof CruVideoViewHolder)
        {

        }
        if(holder instanceof ResourceViewHolder)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }
}
