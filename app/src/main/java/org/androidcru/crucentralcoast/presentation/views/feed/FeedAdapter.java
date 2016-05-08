package org.androidcru.crucentralcoast.presentation.views.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;
import org.androidcru.crucentralcoast.presentation.viewmodels.FeedState;
import org.androidcru.crucentralcoast.presentation.views.events.CruEventViewHolder;
import org.androidcru.crucentralcoast.presentation.views.resources.ResourceViewHolder;
import org.androidcru.crucentralcoast.presentation.views.videos.CruVideoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Dateable> rawItems;
    private List<FeedState<Dateable>> items;

    private final int CRU_EVENT = 0;
    private final int YOUTUBE_VIDEO = 1;
    private final int RESOURCE = 2;

    private RecyclerView.LayoutManager layoutManager;

    public FeedAdapter(List<Dateable> items, RecyclerView.LayoutManager layoutManager)
    {
        this.rawItems = items;
        this.items = new ArrayList<>();
        for(Dateable d : items)
        {
            this.items.add(new FeedState<>(d));
        }
        this.layoutManager = layoutManager;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case CRU_EVENT:
                return new CruEventViewHolder(inflater.inflate(R.layout.card_event, parent, false), this, layoutManager);
            case YOUTUBE_VIDEO:
                return new CruVideoViewHolder(inflater.inflate(R.layout.card_video, parent, false), this, layoutManager);
            default:
                return new ResourceViewHolder(inflater.inflate(R.layout.item_resource, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(items.get(position).model instanceof CruEvent)
        {
            return CRU_EVENT;
        }
        if(items.get(position).model instanceof Snippet)
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
            CruEventViewHolder viewHolder = (CruEventViewHolder) holder;
            viewHolder.bind((ExpandableState) items.get(position));
        }
        if(holder instanceof CruVideoViewHolder)
        {
            CruVideoViewHolder viewHolder = (CruVideoViewHolder) holder;
            viewHolder.bindDatedVideo((ExpandableState) items.get(position));
        }
        if(holder instanceof ResourceViewHolder)
        {
            ResourceViewHolder viewHolder = (ResourceViewHolder) holder;
            viewHolder.bind((Resource) items.get(position).model);
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void syncItems()
    {
        int oldSize = items.size();
        int newSize = rawItems.size();
        for(int i = oldSize; i < newSize; i++)
        {
            this.items.add(new FeedState<>(rawItems.get(i)));
        }
        notifyItemRangeInserted(oldSize, newSize);
    }
}
