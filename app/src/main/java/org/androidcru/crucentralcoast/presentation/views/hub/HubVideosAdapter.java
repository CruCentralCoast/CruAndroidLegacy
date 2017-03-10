package org.androidcru.crucentralcoast.presentation.views.hub;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.views.videos.CruVideoViewHolder;
import org.androidcru.crucentralcoast.presentation.views.videos.VideosAdapter;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class HubVideosAdapter extends VideosAdapter {

    private Context mContext;

    public HubVideosAdapter(Context context, List<Snippet> videos, LinearLayoutManager layoutManager) {
        super(videos, layoutManager);
        mContext = context;
    }

    @Override
    public CruVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(CruVideoViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.rootView.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                mContext.getResources().getDisplayMetrics());
        holder.rootView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230,
                mContext.getResources().getDisplayMetrics());
        holder.videoChev.setVisibility(View.GONE);
        holder.animatingLayout.setOnClickListener(null);
    }

    @Override
    public void setVideos(List<Snippet> cruVideos) {
        super.setVideos(cruVideos);
    }

    @Override
    public void updateViewExpandedStates() {
        super.updateViewExpandedStates();
    }
}
