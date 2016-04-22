package org.androidcru.crucentralcoast.presentation.views.videos;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.SearchResult;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<CruVideoViewHolder>
{
    // Search results from Cru's YouTube channel
    private ArrayList<ExpandableState<SearchResult>> videosWithStates;
    private List<SearchResult> videos;

    private LinearLayoutManager layoutManager;

    public VideosAdapter(List<SearchResult> videos, LinearLayoutManager layoutManager)
    {
        this.videos = videos;

        // Initialize expansion states to retracted for every view
        this.videosWithStates = new ArrayList<>();
        for(int i = 0; i < videos.size(); ++i)
        {
            this.videosWithStates.add(new ExpandableState<SearchResult>(videos.get(i)));
        }

        this.layoutManager = layoutManager;
    }

    @Override
    public CruVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CruVideoViewHolder(inflater.inflate(R.layout.card_video, parent, false), this, layoutManager);
    }

    @Override
    public int getItemCount()
    {
        return videosWithStates.size();
    }

    @Override
    public void onBindViewHolder(CruVideoViewHolder holder, int position)
    {
        holder.bindSearchResult(videosWithStates.get(position));
    }

    public void updateViewExpandedStates()
    {
        while (videosWithStates.size() < videos.size())
        {
            videosWithStates.add(new ExpandableState<>(videos.get(videosWithStates.size())));
        }
    }
}
