package org.androidcru.crucentralcoast.presentation.views.videos;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<CruVideoViewHolder> {
    // Search results from Cru's YouTube channel
    private ArrayList<ExpandableState<Snippet>> videosWithStates;
    private List<Snippet> videos;

    private LinearLayoutManager layoutManager;

    public VideosAdapter(List<Snippet> videos, LinearLayoutManager layoutManager) {
        this.videos = videos;

        // Initialize expansion states to retracted for every view
        this.videosWithStates = new ArrayList<>();
        for (int i = 0; i < videos.size(); ++i) {
            this.videosWithStates.add(new ExpandableState<>(videos.get(i)));
        }

        this.layoutManager = layoutManager;
    }

    @Override
    public CruVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CruVideoViewHolder(inflater.inflate(R.layout.card_video, parent, false), this, layoutManager);
    }

    @Override
    public int getItemCount() {
        return videosWithStates.size();
    }

    @Override
    public void onBindViewHolder(CruVideoViewHolder holder, int position) {
        holder.bindSnippet(videosWithStates.get(position));
    }

    public void setVideos(List<Snippet> cruVideos) {
        videosWithStates.clear();
        for (Snippet video : cruVideos) {
            videosWithStates.add(new ExpandableState<>(video));
        }
        notifyDataSetChanged();
    }

    public void updateViewExpandedStates() {
        while (videosWithStates.size() < videos.size()) {
            videosWithStates.add(new ExpandableState<>(videos.get(videosWithStates.size())));
        }
    }
}
